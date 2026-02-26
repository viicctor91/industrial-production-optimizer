package com.example.optimizer.service;

import com.example.optimizer.dto.ProductionSuggestionResponse;
import com.example.optimizer.dto.RawMaterialResponse;
import com.example.optimizer.entity.ProductEntity;
import com.example.optimizer.entity.ProductMaterialEntity;
import com.example.optimizer.entity.RawMaterialEntity;
import com.example.optimizer.mapper.Mapper;
import com.example.optimizer.repository.ProductRepository;
import com.example.optimizer.repository.RawMaterialRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionPlannerServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private ProductionPlannerService service;

    private RawMaterialEntity iron;
    private RawMaterialEntity coal;

    @BeforeEach
    void setUp() {
        iron = RawMaterialEntity.builder()
                .id(UUID.randomUUID())
                .code("IRON")
                .name("Iron")
                .unit("kg")
                .quantityInStock(new BigDecimal("100"))
                .build();

        coal = RawMaterialEntity.builder()
                .id(UUID.randomUUID())
                .code("COAL")
                .name("Coal")
                .unit("kg")
                .quantityInStock(new BigDecimal("50"))
                .build();
        
        // Mock mapper behavior for remaining stock
        when(mapper.toDto(any(RawMaterialEntity.class))).thenAnswer(invocation -> {
            RawMaterialEntity entity = invocation.getArgument(0);
            RawMaterialResponse dto = new RawMaterialResponse();
            dto.setId(entity.getId());
            dto.setCode(entity.getCode());
            return dto;
        });
    }

    @Test
    void suggestPlan_NoStock_ReturnsEmptyPlan() {
        iron.setQuantityInStock(BigDecimal.ZERO);
        coal.setQuantityInStock(BigDecimal.ZERO);

        ProductEntity sword = createProduct("SWORD", "Sword", new BigDecimal("100"));
        addComposition(sword, iron, new BigDecimal("2"));

        when(productRepository.listAll()).thenReturn(List.of(sword));
        when(rawMaterialRepository.listAll()).thenReturn(List.of(iron, coal));

        ProductionSuggestionResponse response = service.suggestPlan();

        assertTrue(response.getSuggestedItems().isEmpty());
        assertBigDecimalEquals(BigDecimal.ZERO, response.getTotalValue());
    }

    @Test
    void suggestPlan_SingleProduct_CorrectMaxUnits() {
        // Iron: 100, Sword needs 2 Iron -> Max 50 swords
        ProductEntity sword = createProduct("SWORD", "Sword", new BigDecimal("10.0"));
        addComposition(sword, iron, new BigDecimal("2.0"));

        when(productRepository.listAll()).thenReturn(List.of(sword));
        when(rawMaterialRepository.listAll()).thenReturn(List.of(iron));

        ProductionSuggestionResponse response = service.suggestPlan();

        assertEquals(1, response.getSuggestedItems().size());
        assertEquals("SWORD", response.getSuggestedItems().get(0).getProductCode());
        assertEquals(50, response.getSuggestedItems().get(0).getUnitsToProduce());
        assertBigDecimalEquals(new BigDecimal("500.0"), response.getTotalValue());
    }

    @Test
    void suggestPlan_CompositionLimits_ScarcestMaterial() {
        // Iron: 100, Coal: 50
        // Steel needs 2 Iron and 2 Coal
        // Iron limit: 100/2 = 50
        // Coal limit: 50/2 = 25
        // Max units should be 25
        ProductEntity steel = createProduct("STEEL", "Steel", new BigDecimal("20.0"));
        addComposition(steel, iron, new BigDecimal("2.0"));
        addComposition(steel, coal, new BigDecimal("2.0"));

        when(productRepository.listAll()).thenReturn(List.of(steel));
        when(rawMaterialRepository.listAll()).thenReturn(List.of(iron, coal));

        ProductionSuggestionResponse response = service.suggestPlan();

        assertEquals(1, response.getSuggestedItems().size());
        assertEquals(25, response.getSuggestedItems().get(0).getUnitsToProduce());
        assertBigDecimalEquals(new BigDecimal("500.0"), response.getTotalValue());
    }

    @Test
    void suggestPlan_ConflictScenario_HigherPricePrioritized() {
        // Iron: 100
        // Expensive Sword (Price 50, needs 10 Iron) -> Limit 10
        // Cheap Dagger (Price 10, needs 5 Iron) -> Limit 20
        
        // If we produce Expensive Sword first:
        // Produce 10 units. Uses 100 Iron. Remaining Iron: 0.
        // Total Value: 10 * 50 = 500.
        
        // If we produce Cheap Dagger first (wrong):
        // Produce 20 units. Uses 100 Iron. Remaining Iron: 0.
        // Total Value: 20 * 10 = 200.
        
        // Algorithm should pick Expensive Sword.
        
        ProductEntity expensive = createProduct("EXP", "Expensive", new BigDecimal("50.0"));
        addComposition(expensive, iron, new BigDecimal("10.0"));

        ProductEntity cheap = createProduct("CHP", "Cheap", new BigDecimal("10.0"));
        addComposition(cheap, iron, new BigDecimal("5.0"));

        when(productRepository.listAll()).thenReturn(Arrays.asList(cheap, expensive)); // Order shouldn't matter as service sorts
        when(rawMaterialRepository.listAll()).thenReturn(List.of(iron));

        ProductionSuggestionResponse response = service.suggestPlan();

        assertEquals(1, response.getSuggestedItems().size());
        assertEquals("EXP", response.getSuggestedItems().get(0).getProductCode());
        assertEquals(10, response.getSuggestedItems().get(0).getUnitsToProduce());
        assertBigDecimalEquals(new BigDecimal("500.00"), response.getTotalValue());
    }

    @Test
    void suggestPlan_TieBreakers_Deterministic() {
        // Same Price.
        // Product A: Needs 1 material.
        // Product B: Needs 2 materials.
        // Should pick A first (Simpler).
        
        ProductEntity complex = createProduct("B_COMPLEX", "Complex", new BigDecimal("10.0"));
        addComposition(complex, iron, new BigDecimal("1.0"));
        addComposition(complex, coal, new BigDecimal("1.0"));

        ProductEntity simple = createProduct("A_SIMPLE", "Simple", new BigDecimal("10.0"));
        addComposition(simple, iron, new BigDecimal("1.0"));

        when(productRepository.listAll()).thenReturn(Arrays.asList(complex, simple));
        when(rawMaterialRepository.listAll()).thenReturn(List.of(iron, coal));

        // Logic:
        // Simple comes first.
        // Iron: 100.
        // Simple needs 1 Iron. Max 100.
        // Produce 100 Simple. Consumes 100 Iron.
        // Remaining Iron: 0.
        // Complex needs 1 Iron. Max 0.
        
        // If Complex came first:
        // Coal: 50, Iron: 100. Max 50.
        // Produce 50 Complex. Consumes 50 Iron.
        // Remaining Iron: 50.
        // Simple needs 1 Iron. Max 50.
        // Total Value would be 50*10 + 50*10 = 1000.
        
        // Wait, total value is same (1000). But requirement says: "fewer total required raw material lines ASC (simpler first)".
        // So Simple MUST be first.
        
        ProductionSuggestionResponse response = service.suggestPlan();
        
        assertEquals(1, response.getSuggestedItems().size());
        assertEquals("A_SIMPLE", response.getSuggestedItems().get(0).getProductCode());
        assertEquals(100, response.getSuggestedItems().get(0).getUnitsToProduce());
    }

    private ProductEntity createProduct(String code, String name, BigDecimal price) {
        return ProductEntity.builder()
                .id(UUID.randomUUID())
                .code(code)
                .name(name)
                .price(price)
                .build();
    }

    private void addComposition(ProductEntity product, RawMaterialEntity rawMaterial, BigDecimal quantity) {
        ProductMaterialEntity pm = ProductMaterialEntity.builder()
                .product(product)
                .rawMaterial(rawMaterial)
                .quantityRequiredPerUnit(quantity)
                .build();
        product.getComposition().add(pm);
    }

    private static void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        assertTrue(expected.compareTo(actual) == 0, "Expected " + expected + " but was " + actual);
    }
}
