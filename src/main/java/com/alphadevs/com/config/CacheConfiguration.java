package com.alphadevs.com.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.alphadevs.com.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.alphadevs.com.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.alphadevs.com.domain.User.class.getName());
            createCache(cm, com.alphadevs.com.domain.Authority.class.getName());
            createCache(cm, com.alphadevs.com.domain.User.class.getName() + ".authorities");
            createCache(cm, com.alphadevs.com.domain.Products.class.getName());
            createCache(cm, com.alphadevs.com.domain.Desings.class.getName());
            createCache(cm, com.alphadevs.com.domain.Job.class.getName());
            createCache(cm, com.alphadevs.com.domain.Job.class.getName() + ".details");
            createCache(cm, com.alphadevs.com.domain.Job.class.getName() + ".assignedTos");
            createCache(cm, com.alphadevs.com.domain.JobDetais.class.getName());
            createCache(cm, com.alphadevs.com.domain.JobStatus.class.getName());
            createCache(cm, com.alphadevs.com.domain.Items.class.getName());
            createCache(cm, com.alphadevs.com.domain.PurchaseOrder.class.getName());
            createCache(cm, com.alphadevs.com.domain.PurchaseOrder.class.getName() + ".details");
            createCache(cm, com.alphadevs.com.domain.PurchaseOrderDetails.class.getName());
            createCache(cm, com.alphadevs.com.domain.GoodsReceipt.class.getName());
            createCache(cm, com.alphadevs.com.domain.GoodsReceipt.class.getName() + ".details");
            createCache(cm, com.alphadevs.com.domain.GoodsReceipt.class.getName() + ".linkedPOs");
            createCache(cm, com.alphadevs.com.domain.GoodsReceiptDetails.class.getName());
            createCache(cm, com.alphadevs.com.domain.CashBook.class.getName());
            createCache(cm, com.alphadevs.com.domain.DocumentType.class.getName());
            createCache(cm, com.alphadevs.com.domain.Location.class.getName());
            createCache(cm, com.alphadevs.com.domain.Customer.class.getName());
            createCache(cm, com.alphadevs.com.domain.Supplier.class.getName());
            createCache(cm, com.alphadevs.com.domain.Worker.class.getName());
            createCache(cm, com.alphadevs.com.domain.ExUser.class.getName());
            createCache(cm, com.alphadevs.com.domain.Stock.class.getName());
            createCache(cm, com.alphadevs.com.domain.Company.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
