package org.example.ledgerapi;

/**
 * @author liuxu
 * @date 2021/10/30 22:07
 */

@FunctionalInterface
public interface StateDeserializer {
    State deserialize(byte[] buffer);
}
