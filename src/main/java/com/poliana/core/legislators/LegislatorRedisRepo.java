package com.poliana.core.legislators;

import org.apache.log4j.Logger;
import org.msgpack.MessagePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author David Gilmore
 * @date 2/28/14
 */
@Repository
public class LegislatorRedisRepo {

    Environment env;

    private JedisPool jedisPool;

    private String TERMS_BY_BIOGUIDE;
    private String TERMS_BY_LIS;
    private String TERMS_BY_THOMAS;
    private String TERMS_BY_MONGO;

    private static final Logger logger = Logger.getLogger(LegislatorRedisRepo.class);


    /**
     * Return a list of legislator objects for every term a given legislator has served
     * @param bioguideId
     * @return
     */
    public List<Legislator> getLegislatorTermsByBioguide(String bioguideId) {

        String key = TERMS_BY_BIOGUIDE + bioguideId;
        return getLegislatorTermsByKey(key);
    }

    /**
     * Return a list of legislator objects for every term a given legislator has served
     * @param thomasId
     * @return
     */
    public List<Legislator> getLegislatorTermsByThomas(String thomasId) {

        String key = TERMS_BY_THOMAS + thomasId;
        return getLegislatorTermsByKey(key);
    }

    /**
     * Return a list of legislator objects for every term a given legislator has served
     * @param lisId
     * @return
     */
    public List<Legislator> getLegislatorTermsByLis(String lisId) {

        String key = TERMS_BY_LIS + lisId;
        return getLegislatorTermsByKey(key);
    }

    /**
     * Return a list of legislator objects for every term a given legislator has served
     * @param mongoId
     * @return
     */
    public List<Legislator> getLegislatorTermsByMongoId(String mongoId) {

        String key = TERMS_BY_MONGO + mongoId;
        return getLegislatorTermsByKey(key);
    }


    /**
     * Given a redis key, return a list lof legislator objects. Assumes the id has been constructed properly
     * @param key
     * @return
     */
    public List<Legislator> getLegislatorTermsByKey(String key) {

        Jedis jedis = jedisPool.getResource();
        MessagePack messagePack = new MessagePack();
        List<Legislator> legislators = new LinkedList<>();
        try {
            List<byte[]> legislatorBytes = jedis.lrange(messagePack.write(key), 0, -1);
            for (byte[] l : legislatorBytes) {
                Legislator legislator = messagePack.read(l, Legislator.class);
                legislators.add(legislator);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (JedisConnectionException e) {
            if (null != jedis) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (null != jedis)
                jedisPool.returnResource(jedis);
        }
        return legislators;
    }

    /**
     * Using Redis, save legislator term lists with bioguide, lis, thomas, and mongo ids as keys.
     */
    public void saveLegislatorTerms(Iterator<Legislator> legislatorIterator) {

        Jedis jedis = jedisPool.getResource();

        String bioguideKey;
        String thomasKey;
        String lisKey;
        String mongoKey;
        MessagePack messagePack = new MessagePack();
        while (legislatorIterator.hasNext()) {
            Legislator legislator = legislatorIterator.next();
            bioguideKey = TERMS_BY_BIOGUIDE + legislator.getBioguideId();
            thomasKey = TERMS_BY_THOMAS + legislator.getThomasId();
            lisKey = TERMS_BY_LIS + legislator.getLisId();
            mongoKey = TERMS_BY_MONGO + legislator.getId();
            try {
                if (!jedis.exists(messagePack.write(bioguideKey)))
                    jedis.lpush(messagePack.write(bioguideKey), messagePack.write(legislator));
                if (!jedis.exists(messagePack.write(thomasKey)))
                    jedis.lpush(messagePack.write(thomasKey), messagePack.write(legislator));
                if (!jedis.exists(messagePack.write(lisKey)))
                    jedis.lpush(messagePack.write(lisKey), messagePack.write(legislator));
                if (!jedis.exists(messagePack.write(mongoKey)))
                    jedis.lpush(messagePack.write(mongoKey), messagePack.write(legislator));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Autowired
    public void setRedisNameSpace(Environment env) {

        this.env = env;

        this.TERMS_BY_BIOGUIDE = env.getProperty("terms.by.bioguide");
        this.TERMS_BY_LIS = env.getProperty("terms.by.lis");
        this.TERMS_BY_THOMAS = env.getProperty("terms.by.thomas");
        this.TERMS_BY_MONGO = env.getProperty("terms.by.mongo");
    }
}
