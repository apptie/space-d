package com.dnd.spaced.global.auth.encryptor;

import lombok.Getter;

@Getter
public enum AesBit {

    BIT_128(128),
    BIT_192(192),
    BIT_256(256);

    private final int bitLength;

    AesBit(int bitLength) {
        this.bitLength = bitLength;
    }
}
