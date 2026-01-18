package com.caching

import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.ClientConfig
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.IMap
import groovy.util.logging.Slf4j
import jakarta.inject.Singleton

@Singleton
@Slf4j
class HazelcastClientConfig {
    final HazelcastInstance hazelcastInstance

    HazelcastClientConfig() {
        ClientConfig clientConfig = new ClientConfig()
        clientConfig.setClusterName("dev")
        hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig)
        log.info("Connected to cluster: ${hazelcastInstance.getCluster().getMembers()*.address}")
    }

//    Object getValue(String cacheKey, String cacheName, Closure fetcher){
//        IMap map = hazelcastInstance.getMap(cacheName)
//
//        if(map.containsKey(cacheKey)){
//            log.info("Cache hit ${cacheName} ${cacheKey}")
//            return map.get(cacheKey)
//        }
//
//        log.info("Cache miss ${cacheName} ${cacheKey}")
//        Object value = fetcher.call()
//        map.put(cacheKey, value)
//        return value
//    }

    Object getValue(String cacheKey, String cacheName, Closure fetcher){
        IMap map = hazelcastInstance.getMap(cacheName)

        Object value = map.get(cacheKey)
        if(value != null){
            log.info("Cache hit ${cacheName} ${cacheKey}")
            return value
        }

        log.info("Cache miss ${cacheName} ${cacheKey}")
        value = fetcher.call()

        if(value != null){
            map.put(cacheKey, value)
        }
        return value
    }

    void putValue(String cacheKey, String cacheName, Object value){
        hazelcastInstance.getMap(cacheName).put(cacheKey, value)
        log.info("Cache updated ${cacheName} ${cacheKey}")
    }

    void evict(String cacheKey, String cacheName){
        hazelcastInstance.getMap(cacheName).remove(cacheKey)
        log.info("Cache evicted ${cacheName} ${cacheKey}")
    }
}