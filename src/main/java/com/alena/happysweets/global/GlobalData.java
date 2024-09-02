package com.alena.happysweets.global;

import com.alena.happysweets.model.Product;

import java.util.ArrayList;
import java.util.List;
//Class created to manage cart
public class GlobalData {
    public static List<Product> cart;
    static{
        cart = new ArrayList<>();
    }
}
