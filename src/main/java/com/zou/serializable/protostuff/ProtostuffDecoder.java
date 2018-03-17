package com.zou.serializable.protostuff;

import com.zou.serializable.message.MessageCodeHelper;
import com.zou.serializable.message.MessageDecoder;

public class ProtostuffDecoder   extends MessageDecoder{

    public ProtostuffDecoder(MessageCodeHelper helper) {
        super(helper);
    }
}
