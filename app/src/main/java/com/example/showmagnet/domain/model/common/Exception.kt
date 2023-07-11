package com.example.showmagnet.domain.model.common

import java.io.IOException

class NetworkUnavailableException(message: String = "No network available :(") :
    IOException(message)