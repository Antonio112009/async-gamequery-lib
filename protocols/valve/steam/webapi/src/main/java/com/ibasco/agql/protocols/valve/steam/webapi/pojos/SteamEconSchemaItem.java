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

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SteamEconSchemaItem {
    private String name;
    @SerializedName("defindex")
    private int defIndex;
    @SerializedName("item_class")
    private String itemClass;
    @SerializedName("item_type_name")
    private String itemTypeName;
    @SerializedName("item_name")
    private String itemName;
    @SerializedName("item_description")
    private String itemDescription;
    @SerializedName("proper_name")
    private boolean properName;
    @SerializedName("item_slot")
    private String itemSlot; //TODO: use enum instead?
    @SerializedName("model_player")
    private String modelPlayer;
    @SerializedName("item_quality")
    private int itemQuality;
    @SerializedName("image_inventory")
    private String imageInventory;
    @SerializedName("min_ilevel")
    private int minItemLevel;
    @SerializedName("max_ilevel")
    private int maxItemLevel;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("image_url_large")
    private String imageUrlLarge;
    @SerializedName("drop_type ")
    private String dropType;
    @SerializedName("item_set")
    private String itemSet;
    @SerializedName("holiday_restriction")
    private String holidayRestriction;
    @SerializedName("craft_class")
    private String craftClass;
    @SerializedName("craft_material_type")
    private String craftMaterialType;
    private SteamEconSchemaCapabilities capabilities;
    @SerializedName("used_by_classes")
    private List<String> usedByClasses = new ArrayList<>();
    private List<SteamEconSchemaItemAttribute> attributes = new ArrayList<>();
    /**
     * An object that describes class specific loadout slots for the item if applicable.
     */
    @SerializedName("per_class_loadout_slots")
    private Map<String, String> perClassLoadoutSlots;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefIndex() {
        return defIndex;
    }

    public void setDefIndex(int defIndex) {
        this.defIndex = defIndex;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public boolean isProperName() {
        return properName;
    }

    public void setProperName(boolean properName) {
        this.properName = properName;
    }

    public String getItemSlot() {
        return itemSlot;
    }

    public void setItemSlot(String itemSlot) {
        this.itemSlot = itemSlot;
    }

    public String getModelPlayer() {
        return modelPlayer;
    }

    public void setModelPlayer(String modelPlayer) {
        this.modelPlayer = modelPlayer;
    }

    public int getItemQuality() {
        return itemQuality;
    }

    public void setItemQuality(int itemQuality) {
        this.itemQuality = itemQuality;
    }

    public String getImageInventory() {
        return imageInventory;
    }

    public void setImageInventory(String imageInventory) {
        this.imageInventory = imageInventory;
    }

    public int getMinItemLevel() {
        return minItemLevel;
    }

    public void setMinItemLevel(int minItemLevel) {
        this.minItemLevel = minItemLevel;
    }

    public int getMaxItemLevel() {
        return maxItemLevel;
    }

    public void setMaxItemLevel(int maxItemLevel) {
        this.maxItemLevel = maxItemLevel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrlLarge() {
        return imageUrlLarge;
    }

    public void setImageUrlLarge(String imageUrlLarge) {
        this.imageUrlLarge = imageUrlLarge;
    }

    public String getDropType() {
        return dropType;
    }

    public void setDropType(String dropType) {
        this.dropType = dropType;
    }

    public String getItemSet() {
        return itemSet;
    }

    public void setItemSet(String itemSet) {
        this.itemSet = itemSet;
    }

    public String getHolidayRestriction() {
        return holidayRestriction;
    }

    public void setHolidayRestriction(String holidayRestriction) {
        this.holidayRestriction = holidayRestriction;
    }

    public String getCraftClass() {
        return craftClass;
    }

    public void setCraftClass(String craftClass) {
        this.craftClass = craftClass;
    }

    public String getCraftMaterialType() {
        return craftMaterialType;
    }

    public void setCraftMaterialType(String craftMaterialType) {
        this.craftMaterialType = craftMaterialType;
    }

    public SteamEconSchemaCapabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(SteamEconSchemaCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public List<String> getUsedByClasses() {
        return usedByClasses;
    }

    public void setUsedByClasses(List<String> usedByClasses) {
        this.usedByClasses = usedByClasses;
    }

    public List<SteamEconSchemaItemAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SteamEconSchemaItemAttribute> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getPerClassLoadoutSlots() {
        return perClassLoadoutSlots;
    }

    public void setPerClassLoadoutSlots(Map<String, String> perClassLoadoutSlots) {
        this.perClassLoadoutSlots = perClassLoadoutSlots;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
