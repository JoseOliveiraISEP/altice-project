package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/labseq")
public class LabseqCalculator {

    // Cache to store previously computed values
    private final Map<Integer, BigInteger> cache = new ConcurrentHashMap<>();

    public LabseqCalculator() {
        cache.put(0, BigInteger.ZERO); // l(0) = 0
        cache.put(1, BigInteger.ONE); // l(1) = 1
        cache.put(2, BigInteger.ZERO); // l(2) = 0
        cache.put(3, BigInteger.ONE); // l(3) = 1
    }

    @GET
    @Path("/{n}")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Get LabSeq value",
        description = "Returns the LabSeq sequence value for the given index n."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "LabSeq value returned successfully",
            content = @Content(mediaType = "text/plain",
                schema = @Schema(implementation = String.class))
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid index. n must be non-negative."
        )
    })
    public String calc_labseq(int n) {
        if(n < 0) {
            throw new WebApplicationException("Number must be non-negative", Response.Status.BAD_REQUEST);
        }

        BigInteger value = calcLabSeq(n);
        return String.valueOf(value);
    }

    private BigInteger calcLabSeq(int n) {
        if(n > 3) {
            // Will only calculate for values not already cached
            for (int i = 4; i <= n; i++) {
                cache.computeIfAbsent(i, key -> cache.get(key - 4).add(cache.get(key - 3))); // l(n) = l(n-4) + l(n-3)
            }
        }

        return cache.get(n);
    }
}
