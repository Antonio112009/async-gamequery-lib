/*
 * MIT License
 *
 * Copyright (c) 2018 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.protocols.valve.steam.master.client;

import com.ibasco.agql.core.client.AbstractGameServerClient;
import com.ibasco.agql.core.enums.RequestPriority;
import com.ibasco.agql.core.exceptions.AsyncGameLibUncheckedException;
import com.ibasco.agql.core.functions.TriConsumer;
import com.ibasco.agql.core.utils.ConcurrentUtils;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerFilter;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerMessenger;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerRequest;
import com.ibasco.agql.protocols.valve.steam.master.MasterServerResponse;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerRegion;
import com.ibasco.agql.protocols.valve.steam.master.enums.MasterServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>Queries Valve Master Server to retrieve a list of game servers</p>
 */
public class MasterServerQueryClient extends AbstractGameServerClient<MasterServerRequest, MasterServerResponse, MasterServerMessenger> {
    private static final Logger log = LoggerFactory.getLogger(MasterServerQueryClient.class);

    public MasterServerQueryClient() {
        super(new MasterServerMessenger());
    }

    /**
     * <p>A helper to determine if the address is a terminator type address</p>
     *
     * @param address
     *         The {@link InetSocketAddress} of the source server
     *
     * @return true if the {@link InetSocketAddress} supplied is a terminator address
     */
    private static boolean isIpTerminator(InetSocketAddress address) {
        return "0.0.0.0".equals(address.getAddress().getHostAddress()) && address.getPort() == 0;
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server.</p>
     *
     * @param region
     *         A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter
     *         A {@link MasterServerFilter} representing a set of filters to be used by the query
     *
     * @return A {@link CompletableFuture} containing a {@link Vector} of {@link InetSocketAddress}.
     *
     * @see #getServerList(MasterServerType, MasterServerRegion, MasterServerFilter, TriConsumer)
     */
    public CompletableFuture<Vector<InetSocketAddress>> getServerList(final MasterServerType type, final MasterServerRegion region, final MasterServerFilter filter) {
        return getServerList(type, region, filter, null);
    }

    /**
     * <p>Retrieves a list of servers from the Steam Master Server.</p>
     *
     * @param type
     *         A {@link MasterServerType} to indicate which type of servers the master server should return
     * @param region
     *         A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter
     *         A {@link MasterServerFilter} representing a set of filters to be used by the query
     * @param callback
     *         A {@link TriConsumer} that will be invoked repeatedly for partial response
     *
     * @return A {@link CompletableFuture} containing a {@link Vector} of {@link InetSocketAddress}.
     *
     * @see #getServerList(MasterServerType, MasterServerRegion, MasterServerFilter)
     */
    public CompletableFuture<Vector<InetSocketAddress>> getServerList(final MasterServerType type, final MasterServerRegion region, final MasterServerFilter filter, final TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        //As per protocol specs, this get required as our starting seed address
        InetSocketAddress startAddress = new InetSocketAddress("0.0.0.0", 0);
        return CompletableFuture.supplyAsync(() -> this.getServersFromStartAddress(startAddress, type, region, filter, callback));
    }

    /**
     * <p>A blocking function to retrieve a list of servers from the valve master server.</p>
     *
     * @param startAddress
     *         The start IP Address. Use 0.0.0.0:0 to get a list from the beggining.
     * @param type
     *         A {@link MasterServerType} to indicate which type of servers the master server should return
     * @param region
     *         A {@link MasterServerRegion} value that specifies which server region the master server should return
     * @param filter
     *         A {@link MasterServerFilter} representing a set of filters to be used by the query
     * @param callback
     *         A {@link TriConsumer} that will be invoked repeatedly for partial response
     *
     * @return A {@link Vector} containing the server addressess retrieved from the master
     */
    private Vector<InetSocketAddress> getServersFromStartAddress(InetSocketAddress startAddress, MasterServerType type, MasterServerRegion region, MasterServerFilter filter, final TriConsumer<InetSocketAddress, InetSocketAddress, Throwable> callback) {
        final Vector<InetSocketAddress> serverMasterList = new Vector<>();
        final InetSocketAddress destination = type.getMasterAddress();
        final AtomicBoolean done = new AtomicBoolean(false);

        InetSocketAddress _startAddress = startAddress;

        while (!done.get()) {
            log.debug("Getting from master server with seed : " + _startAddress);
            try {
                log.debug("Sending master source with seed: {}:{}, Filter: {}", _startAddress.getAddress().getHostAddress(), _startAddress.getPort(), filter);

                //Send initial query to the master source
                final CompletableFuture<Vector<InetSocketAddress>> p = sendRequest(new MasterServerRequest(destination, region, filter, _startAddress), RequestPriority.HIGH);

                //Retrieve the first batch, timeout after 3 seconds
                final Vector<InetSocketAddress> serverList = p.get(3000, TimeUnit.MILLISECONDS);

                //Iterate through each address and call onComplete responseCallback. Make sure we don't include the last source ip received
                final InetSocketAddress lastServerIp = _startAddress;

                //Filter the address entries and make sure we do not include the last server ip received
                serverList.stream().filter(inetSocketAddress -> (!inetSocketAddress.equals(lastServerIp))).forEachOrdered(ip -> {
                    if (callback != null && !isIpTerminator(ip))
                        callback.accept(ip, destination, null);
                    //Add a fixed delay here. We shouldn't send requests too fast to the master server
                    // there is a chance that we might not receive the end of the list.
                    ConcurrentUtils.sleepUninterrupted(13);
                    serverMasterList.add(ip);
                });

                //Retrieve the last element of the source list and use it as the next seed for the next query
                _startAddress = serverList.lastElement();

                log.debug("Last Server IP Received: {}:{}", _startAddress.getAddress().getHostAddress(), _startAddress.getPort());

                //Did the master send a terminator address?
                // If so, mark as complete
                if (isIpTerminator(_startAddress)) {
                    log.debug("Reached the end of the server list");
                    done.set(true);
                }
                //Thread.sleep(serverList.size() * 15);
            } catch (InterruptedException | TimeoutException e) {
                log.debug("Timeout/Thread Interruption/ExecutionException Occured during retrieval of server list from master");
                done.set(true); //stop looping if we receive a timeout
                if (callback != null)
                    callback.accept(null, destination, e);
            } catch (ExecutionException e) {
                log.debug("ExecutionException occured {}", e);
                if (callback != null)
                    callback.accept(null, destination, e);
                throw new AsyncGameLibUncheckedException(e.getCause());
            }
        } //while

        log.debug("Got a total list of {} servers from master", serverMasterList.size());

        //Returns the complete server list retrieved from the master server
        return serverMasterList;
    }
}
