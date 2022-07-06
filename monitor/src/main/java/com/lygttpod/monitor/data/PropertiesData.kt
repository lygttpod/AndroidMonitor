package com.lygttpod.monitor.data

class PropertiesData(var port: String,
                     var dbName: String,
                     var whiteContentTypes: String?,//ContentType白名单
                     var whiteHosts: String?,//host白名单
                     var blackHosts: String?//host黑名单
)