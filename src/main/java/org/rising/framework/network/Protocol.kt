package org.rising.framework.network

import java.io.IOException

interface Protocol {

    @Throws(IOException::class)
    fun processMessageOnServerSide(message: Message, id: Int)

    fun processMessageOnClientSide(message: Message)
}