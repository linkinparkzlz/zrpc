package com.zou.serializable.kryo;

import com.zou.serializable.message.MessageCodeHelper;
import com.zou.serializable.message.MessageDecoder;


/**
 * @author zoulvzhou
 *
 * 解码器
 */

public class KryoDecoder  extends MessageDecoder{

    public KryoDecoder(MessageCodeHelper helper) {
        super(helper);
    }
}
