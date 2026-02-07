package com.deeprunner.documentsearch.web.filter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String tenant = request.getHeader("X-Tenant-Id");
		Bucket bucket = buckets.computeIfAbsent(tenant,
				t -> Bucket.builder().addLimit(Bandwidth.simple(100, Duration.ofMinutes(1))).build());

		if (!bucket.tryConsume(1)) {
			response.setStatus(429);
			return;
		}

		filterChain.doFilter(request, response);
		
	}
}
