package com.zou.serializable.kryo;

import com.zou.serializable.message.MessageCodeHelper;
import com.zou.serializable.message.MessageEncoder;

public class KryoEncoder extends MessageEncoder {

    public KryoEncoder(MessageCodeHelper helper) {
        super(helper);
    }
}
