package com.petstore.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.petstore.annotations.test.ApiTest;
import com.petstore.model.Category;
import com.petstore.model.Pet;
import com.petstore.model.Tag;
import com.petstore.service.PetStoreService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

@SpringBootTest
public class PetStoreCrudTest {

  @Autowired
  private PetStoreService petStoreService;

  private Pet testPet;
  private Long testPetId;

  @BeforeEach
  void before() {
    Category category = Category.builder()
        .id(1L)
        .name("Köpekler")
        .build();

    List<Tag> tags = Arrays.asList(
        Tag.builder().id(1L).name("oyuncu").build(),
        Tag.builder().id(2L).name("arkadaş canlısı").build()
    );

    testPet = Pet.builder()
        .name("Buddy")
        .status("available")
        .category(category)
        .tags(tags)
        .build();
  }

  @ApiTest
  @Description("Yeni Pet Ekleme")
  void testCreatePet() {
    StepVerifier.create(petStoreService.createPet(testPet))
        .assertNext(createdPet -> {
          assertThat(createdPet.getId()).isNotNull();
          assertThat(createdPet.getName()).isEqualTo("Buddy");
          assertThat(createdPet.getStatus()).isEqualTo("available");
          assertThat(createdPet.getCategory().getName()).isEqualTo("Köpekler");
          testPetId = createdPet.getId();
        })
        .verifyComplete();
  }

  @ApiTest
  @Description("Pet Güncelleme")
  void testUpdatePet() {
    var createdPet = petStoreService.createPet(testPet).block();
    testPetId = createdPet.getId();

    var updatedPet = Pet.builder()
        .id(testPetId)
        .name("Buddy Güncellenmiş")
        .status("pending")
        .photoUrls(createdPet.getPhotoUrls())
        .category(createdPet.getCategory())
        .tags(createdPet.getTags())
        .build();

    StepVerifier.create(petStoreService.updatePet(updatedPet))
        .assertNext(pet -> {
          assertThat(pet.getId()).isEqualTo(testPetId);
          assertThat(pet.getName()).isEqualTo("Buddy Güncellenmiş");
          assertThat(pet.getStatus()).isEqualTo("pending");
        })
        .verifyComplete();
  }

  @ApiTest
  @Description("Status'a Göre Pet Arama")
  void testFindPetsByStatus() {
    petStoreService.createPet(testPet).block();

    StepVerifier.create(petStoreService.findPetsByStatus("available"))
        .assertNext(pets -> {
          assertThat(pets).isNotEmpty();
          assertThat(pets).anyMatch(pet -> "available".equals(pet.getStatus()));
        })
        .verifyComplete();
  }

  @ApiTest
  @Description("Pet Silme")
  void testDeletePet() {
    var createdPet = petStoreService.createPet(testPet).block();
    testPetId = createdPet.getId();

    StepVerifier.create(petStoreService.deletePet(testPetId))
        .verifyComplete();

    StepVerifier.create(petStoreService.getPetById(testPetId))
        .expectError(WebClientResponseException.NotFound.class)
        .verify();
  }

  @ApiTest
  @DisplayName("Geçersiz Pet ID ile Getirme")
  void testGetPetByIdNotFound() {
    var nonExistentId = 999999999L;

    StepVerifier.create(petStoreService.getPetById(nonExistentId))
        .expectError(WebClientResponseException.NotFound.class)
        .verify();
  }


  @ApiTest
  @DisplayName("Boş İsimle Pet Oluşturma")
  void testCreatePetEmptyName() {
    var emptyNamePet = Pet.builder()
        .name("")
        .status("available")
        .build();

    StepVerifier.create(petStoreService.createPet(emptyNamePet))
        .assertNext(createdPet -> {
          assertThat(createdPet.getName()).isEmpty();
        })
        .verifyComplete();
  }

  @ApiTest
  @Description("Geçersiz Status ile Pet Arama")
  void testFindPetsByStatusInvalidStatus() {
    var invalidStatus = "geçersiz_status";

    StepVerifier.create(petStoreService.findPetsByStatus(invalidStatus))
        .assertNext(pets -> {
          assertThat(pets).isEmpty();
        })
        .verifyComplete();
  }

  @ApiTest
  @Description("Negatif Pet ID ile Getirme")
  void testGetPetByIdNegativeId() {
    var negativeId = -1L;

    StepVerifier.create(petStoreService.getPetById(negativeId))
        .expectError(WebClientResponseException.class)
        .verify();
  }

  @ApiTest
  @Description("Olmayan Pet Silme")
  void testDeletePetNotFound() {
    var nonExistentId = 999999999L;

    StepVerifier.create(petStoreService.deletePet(nonExistentId))
        .expectError(WebClientResponseException.NotFound.class)
        .verify();
  }

  @ApiTest
  @Description("Çok Uzun Pet İsmi")
  void testCreatePetVeryLongName() {
    var longName = "A".repeat(1000);

    Pet longNamePet = Pet.builder()
        .name(longName)
        .status("available")
        .build();

    StepVerifier.create(petStoreService.createPet(longNamePet))
        .assertNext(createdPet -> {
          assertThat(createdPet.getName()).isEqualTo(longName);
          assertThat(createdPet.getId()).isNotNull();
        })
        .verifyComplete();
  }
}