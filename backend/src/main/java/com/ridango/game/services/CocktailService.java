package com.ridango.game.services;

import com.ridango.domain.Cocktail;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CocktailService {

    @Value("${cocktail.api.url}")
    private String apiUrl;

    public CocktailService() {
    }

    public Cocktail getCocktail() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(apiUrl, String.class);

        return createCocktail(result);
    }

    private Cocktail createCocktail(String result) throws JSONException {
        List<String> ingredients = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        JSONArray drinksJson = jsonObject.getJSONArray("drinks");
        JSONObject drink = drinksJson.getJSONObject(0);

        String name = drink.getString("strDrink");
        String instructions = drink.getString("strInstructions");
        String category = drink.getString("strCategory");
        String glass = drink.getString("strGlass");
        String imgUrl = drink.getString("strDrinkThumb");

        for (int i = 1; i < 16; i++) {
            String ingredient = drink.getString("strIngredient" + i);
            if (!ingredient.equals("null"))
                ingredients.add(ingredient);
        }
        return new Cocktail(name, instructions, category, glass, ingredients, imgUrl);
    }
}
