package com.ridango.domain;


import java.util.List;

public record Cocktail(String name, String instructions, String category, String glass, List<String> ingredients,
                       String imgUrl) {
}
