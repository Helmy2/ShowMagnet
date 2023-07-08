package com.example.showmagnet.domain.model

import java.io.IOException

class NetworkUnavailableException(message: String = "No network available :(") :
    IOException(message)