package com.usermind.elidetest.spring;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

// Overrides the Spring Framework Web adapter class for Apache Http Components to provide DigestAuth
// see https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientPreemptiveDigestAuthentication.java
public class PreemptiveDigestAuthenticationRequestFactory extends HttpComponentsClientHttpRequestFactory {

   private final AuthCache authCache;

   public PreemptiveDigestAuthenticationRequestFactory(CloseableHttpClient client) {
       super(client);
       this.authCache = new BasicAuthCache();
   }

   public void register(HttpHost target, String realm, String nonce) {
       // Generate DIGEST scheme object, initialize it and add it to the local auth cache
       DigestScheme digestAuth = new DigestScheme();
       // Suppose we already know the realm name
       if (realm != null) {
           digestAuth.overrideParamter("realm", realm);
       }
       // Suppose we already know the expected nonce value
       if (nonce != null) {
           digestAuth.overrideParamter("nonce", nonce);
       }
       authCache.put(target, digestAuth);
   }

   @Override
   protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
       // Add AuthCache to the execution context
       HttpClientContext localContext = HttpClientContext.create();
       localContext.setAuthCache(authCache);
       // reuse the auth cache
       return localContext;
   }
}
