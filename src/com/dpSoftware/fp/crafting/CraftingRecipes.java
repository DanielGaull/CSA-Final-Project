package com.dpSoftware.fp.crafting;

import java.io.File;
import java.util.ArrayList;

import com.dpSoftware.fp.items.ItemStack;
import com.dpSoftware.fp.items.Items;
import com.dpSoftware.fp.util.ResourceLoader;

public class CraftingRecipes {

	private static ArrayList<CraftingRecipe> recipes;
	private static boolean doneInitializing;
	private static ArrayList<String> recipeFiles;
	private static int initIndex;
	
	public static void init() {
		recipes = new ArrayList<>();
		recipeFiles = new ArrayList<>();
		Items[] allItems = Items.class.getEnumConstants();
		for (int i = 0; i < allItems.length; i++) {
			recipeFiles.add(allItems[i].getRecipeDirectory());
		}
	}
	public static void initNext() {
		CraftingRecipe recipe = ResourceLoader.loadRecipe(recipeFiles.get(initIndex));
		if (recipe != null) {
			recipes.add(recipe);
		}
		initIndex++;
		if (!hasNext()) {
			doneInitializing = true;
		}
	}
	public static boolean hasNext() {
		return initIndex < recipeFiles.size();
	}
	public static double getProgress() {
		return 1.0 * initIndex / recipeFiles.size();
	}
	
	public static int recipeCount() {
		return recipes.size();
	}
	public static CraftingRecipe getRecipe(int index) {
		return recipes.get(index);
	}
	public static ArrayList<CraftingRecipe> getRecipes() {
		return recipes;
	}
	
	//public static void outputRecipes() {
		/*
		CraftingRecipe leafSwordRecipe = new CraftingRecipe(new ItemStack(Items.LeafSword, 1), new ItemStack(Items.Wood, 25),
				new ItemStack(Items.Metal, 15), new ItemStack(Items.Leaves, 30));
		System.out.println(leafSwordRecipe.toJSON());
		
		CraftingRecipe desertSwordRecipe = new CraftingRecipe(new ItemStack(Items.DesertSword, 1), new ItemStack(Items.Wood, 15),
				new ItemStack(Items.Metal, 30), new ItemStack(Items.Cactus, 35), new ItemStack(Items.Sand, 50));
		System.out.println("\n\n\n" + desertSwordRecipe.toJSON());
		
		CraftingRecipe cobaltSwordRecipe = new CraftingRecipe(new ItemStack(Items.CobaltSword, 1), new ItemStack(Items.Wood, 10),
				new ItemStack(Items.Metal, 35), new ItemStack(Items.Cobalt, 50));
		System.out.println("\n\n\n" + cobaltSwordRecipe.toJSON());
		
		CraftingRecipe forestShieldRecipe = new CraftingRecipe(new ItemStack(Items.ForestShield, 1), new ItemStack(Items.Metal, 25),
				new ItemStack(Items.Wood, 25), new ItemStack(Items.Leaves, 50));
		System.out.println(forestShieldRecipe.toJSON());*/
		
		/*CraftingRecipe tarantulaEggRecipe = new CraftingRecipe(new ItemStack(Items.TarantulaEgg, 1), new ItemStack(Items.Web, 60),
				new ItemStack(Items.Poison, 25));
		System.out.println(tarantulaEggRecipe.toJSON());*/
		
		/*CraftingRecipe autumnShieldRecipe = new CraftingRecipe(new ItemStack(Items.AutumnShield, 1), new ItemStack(Items.ForestShield, 1),
				new ItemStack(Items.Wood, 75), new ItemStack(Items.Leaves, 250));
		System.out.println(autumnShieldRecipe.toJSON());*/
		
		/*CraftingRecipe glacierSwordRecipe = new CraftingRecipe(new ItemStack(Items.GlacierSword, 1), new ItemStack(Items.Metal, 15),
				new ItemStack(Items.Ice, 25), new ItemStack(Items.Snow, 50));
		System.out.println(glacierSwordRecipe.toJSON());*/
		
		/*CraftingRecipe tarantulaSwordRecipe = new CraftingRecipe(new ItemStack(Items.TarantulaSword, 1), new ItemStack(Items.Metal, 50),
		new ItemStack(Items.BloodSilk, 150), new ItemStack(Items.Web, 300));
		System.out.println(tarantulaSwordRecipe.toJSON());*/
		
		/*CraftingRecipe arcticSwordRecipe = new CraftingRecipe(new ItemStack(Items.ArcticSword, 1), new ItemStack(Items.GlacierSword, 1),
				new ItemStack(Items.Snow, 100), new ItemStack(Items.Ice, 50), new ItemStack(Items.YetiIcicle, 1));
		System.out.println(arcticSwordRecipe.toJSON());*/
		
		/*CraftingRecipe frostTabletRecipe = new CraftingRecipe(new ItemStack(Items.FrostTablet, 1), new ItemStack(Items.Snow, 60),
				new ItemStack(Items.Ice, 30));
		System.out.println(frostTabletRecipe.toJSON());*/
		
		/*CraftingRecipe volcanoSwordRecipe = new CraftingRecipe(new ItemStack(Items.VolcanoSword, 1), new ItemStack(Items.DesertSword, 1),
		  		new ItemStack(Items.Stone, 100), new ItemStack(Items.MoltenRock, 50), new ItemStack(Items.Metal, 25));
		System.out.println(volcanoSwordRecipe.toJSON());*/
		
		/*CraftingRecipe iceShieldRecipe = new CraftingRecipe(new ItemStack(Items.IceShield, 1), new ItemStack(Items.Snow, 50),
				new ItemStack(Items.Ice, 25));
		CraftingRecipe winterShieldRecipe = new CraftingRecipe(new ItemStack(Items.WinterShield, 1), new ItemStack(Items.IceShield, 1),
				new ItemStack(Items.Snow, 100), new ItemStack(Items.Ice, 55));
		System.out.println(iceShieldRecipe.toJSON());
		System.out.println(winterShieldRecipe.toJSON());*/
		
		/*CraftingRecipe blossomShieldRecipe = new CraftingRecipe(new ItemStack(Items.BlossomShield, 1), new ItemStack(Items.Flower, 75),
				new ItemStack(Items.Honey, 25), new ItemStack(Items.Wood, 25));
		CraftingRecipe springShieldRecipe = new CraftingRecipe(new ItemStack(Items.SpringShield, 1), new ItemStack(Items.BlossomShield, 1), 
				new ItemStack(Items.Flower, 150), new ItemStack(Items.Honey, 50));
		System.out.println(blossomShieldRecipe.toJSON());
		System.out.println(springShieldRecipe.toJSON());*/
		
		/*CraftingRecipe sandShieldRecipe = new CraftingRecipe(new ItemStack(Items.SandShield, 1), new ItemStack(Items.Sand, 50), 
				new ItemStack(Items.Cactus, 25));
		CraftingRecipe summerShieldRecipe = new CraftingRecipe(new ItemStack(Items.SummerShield, 1), new ItemStack(Items.SandShield, 1), 
				new ItemStack(Items.Sand, 75), new ItemStack(Items.MoltenRock, 25));
		System.out.println(sandShieldRecipe.toJSON());
		System.out.println(summerShieldRecipe.toJSON());*/
		
		/*CraftingRecipe earthShieldRecipe = new CraftingRecipe(new ItemStack(Items.EarthShield, 1), new ItemStack(Items.AutumnShield, 1),
				new ItemStack(Items.SpringShield, 1), new ItemStack(Items.WinterShield, 1), new ItemStack(Items.SummerShield, 1), 
				new ItemStack(Items.Metal, 50), new ItemStack(Items.Stone, 100));
		System.out.println(earthShieldRecipe.toJSON());*/
		
		/*CraftingRecipe honeyShieldRecipe = new CraftingRecipe(new ItemStack(Items.HoneyShield, 1), new ItemStack(Items.Wood, 25),
				new ItemStack(Items.Honey, 75), new ItemStack(Items.Stinger, 10));
		System.out.println(honeyShieldRecipe.toJSON());*/
		
		/*CraftingRecipe boneSwordRecipe = new CraftingRecipe(new ItemStack(Items.BoneSword, 1), new ItemStack(Items.Metal, 10),
				new ItemStack(Items.Wood, 10), new ItemStack(Items.Bone, 50));
		System.out.println(boneSwordRecipe.toJSON());
		CraftingRecipe skullSwordRecipe = new CraftingRecipe(new ItemStack(Items.SkullSword, 1), new ItemStack(Items.BoneSword, 1),
				new ItemStack(Items.Bone, 65), new ItemStack(Items.Skull, 1), new ItemStack(Items.Ectoplasm, 10));
		System.out.println(skullSwordRecipe.toJSON());
		CraftingRecipe spiritBrandRecipe = new CraftingRecipe(new ItemStack(Items.SpiritBrand, 1), new ItemStack(Items.BoneSword, 1),
				new ItemStack(Items.Ectoplasm, 50));
		System.out.println(spiritBrandRecipe.toJSON());
		CraftingRecipe reaperBladeRecipe = new CraftingRecipe(new ItemStack(Items.ReaperBlade, 1), new ItemStack(Items.SkullSword, 1),
				new ItemStack(Items.SpiritBrand, 1), new ItemStack(Items.Bone, 50), new ItemStack(Items.Ectoplasm, 50), new ItemStack(Items.Skull, 5));
		System.out.println(reaperBladeRecipe.toJSON());*/
		
		/*CraftingRecipe minerShield = new CraftingRecipe(new ItemStack(Items.MinerShield, 1), new ItemStack(Items.Metal, 25), 
				new ItemStack(Items.Stone, 40), new ItemStack(Items.Gem, 3));
		System.out.println(minerShield.toJSON());
		CraftingRecipe gemShield = new CraftingRecipe(new ItemStack(Items.GemShield, 1), new ItemStack(Items.MinerShield, 1), 
				new ItemStack(Items.Metal, 55), new ItemStack(Items.Stone, 110), new ItemStack(Items.Gem, 20));
		System.out.println(gemShield.toJSON());
		CraftingRecipe mountainShield = new CraftingRecipe(new ItemStack(Items.MountainShield, 1), new ItemStack(Items.GemShield, 1), 
				new ItemStack(Items.Metal, 75), new ItemStack(Items.Stone, 200), new ItemStack(Items.Gem, 40));
		System.out.println(mountainShield.toJSON());*/
		
		/*CraftingRecipe waspBlade = new CraftingRecipe(new ItemStack(Items.WaspBlade, 1), new ItemStack(Items.LeafSword, 1),
				new ItemStack(Items.Honey, 75), new ItemStack(Items.Stinger, 20), new ItemStack(Items.Flower, 50), new ItemStack(Items.Leaves, 100));
		System.out.println(waspBlade.toJSON());*/
		
		/*CraftingRecipe ancientSeaSword = new CraftingRecipe(new ItemStack(Items.AncientSeaSword, 1), new ItemStack(Items.Metal, 15),
				new ItemStack(Items.Wood, 20), new ItemStack(Items.Seaweed, 50), new ItemStack(Items.Carp, 30), new ItemStack(Items.Seashell, 25));
		System.out.println(ancientSeaSword.toJSON());
		CraftingRecipe salmonSword = new CraftingRecipe(new ItemStack(Items.SalmonBlade, 1), new ItemStack(Items.Metal, 15),
				new ItemStack(Items.Wood, 20), new ItemStack(Items.Salmon, 55));
		System.out.println(salmonSword.toJSON());
		CraftingRecipe tsunamiSword = new CraftingRecipe(new ItemStack(Items.TsunamiSword, 1), new ItemStack(Items.AncientSeaSword, 1),
				new ItemStack(Items.SalmonBlade, 1), new ItemStack(Items.Salmon, 50), new ItemStack(Items.Carp, 50), new ItemStack(Items.Seaweed, 100), 
				new ItemStack(Items.Seashell, 100));
		System.out.println(tsunamiSword.toJSON());
		
		CraftingRecipe glennSword = new CraftingRecipe(new ItemStack(Items.GlennSword, 1), new ItemStack(Items.TarantulaSword, 1),
				new ItemStack(Items.ArcticSword, 1), new ItemStack(Items.VolcanoSword, 1), new ItemStack(Items.ReaperBlade, 1), 
				new ItemStack(Items.TsunamiSword, 1));
		System.out.println(glennSword.toJSON());*/
		
		/*CraftingRecipe honeyCrystal = new CraftingRecipe(new ItemStack(Items.HoneyCrystal, 1), new ItemStack(Items.Honey, 25));
		System.out.println(honeyCrystal.toJSON());*/
	//}
	
}
