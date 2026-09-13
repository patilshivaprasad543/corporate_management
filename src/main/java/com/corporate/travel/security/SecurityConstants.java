package com.corporate.travel.security;

/**
 * Authority prefix for granular permissions used with {@code @PreAuthorize}.
 */
public final class SecurityConstants {

    public static final String PERMISSION_PREFIX = "PERM_";

    private SecurityConstants() {}

    public static String permissionAuthority(String permissionName) {
        return PERMISSION_PREFIX + permissionName;
    }
}
