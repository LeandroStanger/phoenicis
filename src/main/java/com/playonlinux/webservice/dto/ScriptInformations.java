/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.webservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.playonlinux.utils.OperatingSystem;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScriptInformations {
    public ArrayList<OperatingSystem> getCompatiblesOperatingSystems() {
        return compatiblesOperatingSystems;
    }

    public ArrayList<OperatingSystem> getTestingOperatingSystems() {
        return testingOperatingSystems;
    }

    public Boolean isFree() {
        return free;
    }

    public Boolean isRequiresNoCD() {
        return requiresNoCD;
    }

    public Boolean isHasIcon() {
        return hasIcon;
    }

    private ArrayList<OperatingSystem> compatiblesOperatingSystems;
    private ArrayList<OperatingSystem> testingOperatingSystems;

    private Boolean free;
    private Boolean requiresNoCD;
    private Boolean hasIcon;

}
