package com.example.HappySweets.global;

import com.example.HappySweets.model.Product;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    public static List<Product> cart;
    static{
        cart = new ArrayList<>();
    }
}
