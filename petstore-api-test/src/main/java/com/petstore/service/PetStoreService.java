package com.petstore.service;

import com.petstore.model.Pet;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class PetStoreService {
    
    private final WebClient webClient;
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    
    public PetStoreService() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }
    
    public Mono<Pet> createPet(Pet pet) {
        return webClient.post()
                .uri("/pet")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(pet)
                .retrieve()
                .bodyToMono(Pet.class);
    }
    
    public Mono<Pet> getPetById(Long petId) {
        return webClient.get()
                .uri("/pet/{petId}", petId)
                .retrieve()
                .bodyToMono(Pet.class);
    }

    public Mono<Pet> updatePet(Pet pet) {
        return webClient.get()
            .uri("/pet/{id}", pet.getId())
            .retrieve()
            .bodyToMono(Pet.class)
            .flatMap(existing ->
                webClient.put()
                    .uri("/pet")
                    .bodyValue(pet)
                    .retrieve()
                    .bodyToMono(Pet.class)
            );
    }

    public Mono<Void> deletePet(Long petId) {
        return webClient.delete()
                .uri("/pet/{petId}", petId)
                .retrieve()
                .bodyToMono(Void.class);
    }
    
    public Mono<Pet[]> findPetsByStatus(String status) {
        return webClient.get()
                .uri("/pet/findByStatus?status={status}", status)
                .retrieve()
                .bodyToMono(Pet[].class);
    }
} 