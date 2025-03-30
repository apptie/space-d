package com.dnd.spaced.global.auth.encryptor;

import javax.crypto.Cipher;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class CipherPool {

    private final ObjectPool<Cipher> pool;

    public CipherPool(String transformation) {
        GenericObjectPoolConfig<Cipher> config = createGenericObjectPoolConfig();

        this.pool = new GenericObjectPool<>(new AesCipherPooledObjectFactory(transformation), config);
    }

    private GenericObjectPoolConfig<Cipher> createGenericObjectPoolConfig() {
        GenericObjectPoolConfig<Cipher> config = new GenericObjectPoolConfig<>();

        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMinIdle(2);
        return config;
    }

    public Cipher borrowCipher() throws Exception {
        return pool.borrowObject();
    }

    public void returnCipher(Cipher cipher) throws Exception {
        pool.returnObject(cipher);
    }

    private static class AesCipherPooledObjectFactory extends BasePooledObjectFactory<Cipher> {

        private final String transformation;

        public AesCipherPooledObjectFactory(String transformation) {
            this.transformation = transformation;
        }

        @Override
        public Cipher create() throws Exception {
            return Cipher.getInstance(transformation);
        }

        @Override
        public PooledObject<Cipher> wrap(Cipher cipher) {
            return new DefaultPooledObject<>(cipher);
        }
    }
}
