#
#  generate.py
#
#  Data found in:
#  - src/main/python/data/vanilla_dimensions.json
#  - src/main/python/data/modded_dimensions.json
#
#  This python script generates the JSON files for the following folders:
#  - src/main/resources/data/adm2/dimension/
#  - src/main/resources/data/adm2/worldgen/noise_settings/
#  - src/main/resources/data/adm2/custom_portal_generation/
#  - src/main/resources/data/adm2/worldgen/placed_feature/
#  - src/main/resources/data/adm2/worldgen/biome/
#
#  It is intended to run from "src/main/".
#

import json
import os

import object_dimension
import object_noise_settings
import object_immersive_portal
import object_ore
import object_biome

written_files = 0

# ---------------------------------------------------------------------------------------------------- generate

exclude = ["iceberg.json"]

def empty_directories():
  dimension_directory = "./resources/data/adm2/dimension/"
  noise_settings_directory = "./resources/data/adm2/worldgen/noise_settings/"
  immersive_portal_directory = "./resources/data/adm2/custom_portal_generation/"
  placed_feature_directory = "./resources/data/adm2/worldgen/placed_feature/"
  biome_directory = "./resources/data/adm2/worldgen/biome/"

  removed_files = 0

  for file in os.listdir(dimension_directory):
    if file in exclude:
      continue

    os.remove(f"{dimension_directory}{file}")
    removed_files += 1

  for file in os.listdir(noise_settings_directory):
    if file in exclude:
      continue

    os.remove(f"{noise_settings_directory}{file}")
    removed_files += 1

  for file in os.listdir(immersive_portal_directory):
    if file in exclude:
      continue

    os.remove(f"{immersive_portal_directory}{file}")
    removed_files += 1

  for file in os.listdir(placed_feature_directory):
    if file in exclude:
      continue

    os.remove(f"{placed_feature_directory}{file}")
    removed_files += 1

  for file in os.listdir(biome_directory):
    if file in exclude:
      continue

    os.remove(f"{biome_directory}{file}")
    removed_files += 1

  print(f"Removed {removed_files} files.")

def generate_dimension(dimensionId):
  global written_files

  dimension = object_dimension.get(dimensionId)
  file_path = f"./resources/data/adm2/dimension/{dimensionId}.json"
  
  with open(file_path, "w") as file:
    json.dump(dimension, file, indent=2)

  written_files += 1

def generate_noise_settings(dimensionId, blockId):
  global written_files

  leaves = f"{blockId}".endswith("_leaves")

  noise_settings = object_noise_settings.get(blockId, leaves)
  file_path = f"./resources/data/adm2/worldgen/noise_settings/{dimensionId}.json"
  
  with open(file_path, "w") as file:
    json.dump(noise_settings, file, indent=2)

  written_files += 1

def generate_immersive_portal(dimensionId, blockId, vanilla_dimensions, modded_dimensions):
  global written_files

  immersive_portal_to = object_immersive_portal.get_to(dimensionId, blockId, vanilla_dimensions, modded_dimensions)
  immersive_portal_from = object_immersive_portal.get_from(dimensionId, blockId)

  file_path_to = f"./resources/data/adm2/custom_portal_generation/{dimensionId}_to.json"
  file_path_from = f"./resources/data/adm2/custom_portal_generation/{dimensionId}_from.json"

  with open(file_path_to, "w") as file:
    json.dump(immersive_portal_to, file, indent=2)

  with open(file_path_from, "w") as file:
    json.dump(immersive_portal_from, file, indent=2)

  written_files += 2

def generate_ore(blockIds, ore):
  global written_files

  oreId = ore[0]
  oreSize = ore[1]
  oreCount = ore[2]
  file_name = ore[3]

  ore = object_ore.get(blockIds, oreId, oreSize, oreCount)
  file_path = f"./resources/data/adm2/worldgen/placed_feature/{file_name}.json"

  with open(file_path, "w") as file:
    json.dump(ore, file, indent=2)

  written_files += 1

def generate_biome(ids):
  global written_files

  biome = object_biome.get(ids)
  file_path = f"./resources/data/adm2/worldgen/biome/common.json"

  with open(file_path, "w") as file:
    json.dump(biome, file, indent=2)

  written_files += 1

# ---------------------------------------------------------------------------------------------------- main

def main():
  empty_directories()

  vanilla_dimensions = []
  modded_dimensions = []

  featureBlockIds = []
  featureOres = [
    ("minecraft:tnt", 32, 6, "tnt"),
    ("minecraft:pearlescent_froglight", 12, 4, "pearlescent_froglight"),
    ("minecraft:sand", 20, 2, "sand"),
    ("minecraft:gravel", 20, 2, "gravel"),
    ("adm2:any_dimensional_sand", 8, 4, "adm2_any_dimensional_sand"),
    ("adm2:any_dimensional_coal_ore", 8, 4, "adm2_any_dimensional_coal_ore"),
    ("adm2:any_dimensional_copper_ore", 8, 4, "adm2_any_dimensional_copper_ore"),
    ("adm2:any_dimensional_diamond_ore", 8, 4, "adm2_any_dimensional_diamond_ore"),
    ("adm2:any_dimensional_emerald_ore", 8, 4, "adm2_any_dimensional_emerald_ore"),
    ("adm2:any_dimensional_gold_ore", 8, 4, "adm2_any_dimensional_gold_ore"),
    ("adm2:any_dimensional_iron_ore", 8, 4, "adm2_any_dimensional_iron_ore"),
    ("adm2:any_dimensional_lapis_ore", 8, 4, "adm2_any_dimensional_lapis_ore"),
    ("adm2:any_dimensional_quartz_ore", 8, 4, "adm2_any_dimensional_quartz_ore"),
    ("adm2:any_dimensional_redstone_ore", 8, 4, "adm2_any_dimensional_redstone_ore"),
  ]
  featureNames = [ore[3] for ore in featureOres]
  featureNames.append("iceberg")

  with open("./python/data/vanilla_dimensions.json", "r") as file:
    data = json.load(file)
    
    vanilla_dimensions = data

  with open("./python/data/modded_dimensions.json", "r") as file:
    data = json.load(file)

    modded_dimensions = data

  for id in vanilla_dimensions:
      generate_dimension(f"minecraft__{id}")
      generate_noise_settings(f"minecraft__{id}", f"minecraft:{id}")
      generate_immersive_portal(f"minecraft__{id}", f"minecraft:{id}", vanilla_dimensions, modded_dimensions)

      featureBlockIds.append(f"minecraft:{id}")

  for dimension in modded_dimensions:
    dimensionId = dimension["dimensionId"]
    blockId = dimension["blockId"]

    generate_dimension(dimensionId)
    generate_noise_settings(dimensionId, blockId)
    generate_immersive_portal(dimensionId, blockId, vanilla_dimensions, modded_dimensions)

    featureBlockIds.append(blockId)

  for ore in featureOres:
    generate_ore(featureBlockIds, ore)

  generate_biome(featureNames)

  print(f"Generated {written_files} files.")

if __name__ == "__main__":
  main()
