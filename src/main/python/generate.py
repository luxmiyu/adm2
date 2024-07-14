#
#  generate.py
#
#  Data found in:
#  - src/main/python/data/block_ids.json
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

def generate_dimension(dimension_id):
  global written_files

  dimension = object_dimension.get(dimension_id)
  file_path = f"./resources/data/adm2/dimension/{dimension_id}.json"
  
  with open(file_path, "w") as file:
    json.dump(dimension, file, indent=2)

  written_files += 1

def generate_noise_settings(dimension_id, block_id):
  global written_files

  leaves = f"{block_id}".endswith("_leaves")

  noise_settings = object_noise_settings.get(block_id, leaves)
  file_path = f"./resources/data/adm2/worldgen/noise_settings/{dimension_id}.json"
  
  with open(file_path, "w") as file:
    json.dump(noise_settings, file, indent=2)

  written_files += 1

def generate_immersive_portal(dimension_id, block_id, block_ids):
  global written_files

  immersive_portal_to = object_immersive_portal.get_to(dimension_id, block_id, block_ids)
  immersive_portal_from = object_immersive_portal.get_from(dimension_id, block_id)

  file_path_to = f"./resources/data/adm2/custom_portal_generation/{dimension_id}_to.json"
  file_path_from = f"./resources/data/adm2/custom_portal_generation/{dimension_id}_from.json"

  with open(file_path_to, "w") as file:
    json.dump(immersive_portal_to, file, indent=2)

  with open(file_path_from, "w") as file:
    json.dump(immersive_portal_from, file, indent=2)

  written_files += 2

def generate_ore(block_ids, ore):
  global written_files

  oreId = ore[0]
  oreSize = ore[1]
  oreCount = ore[2]
  file_name = ore[3]

  ore = object_ore.get(block_ids, oreId, oreSize, oreCount)
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

  block_ids = []

  feature_block_ids = []
  feature_ores = [
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
  feature_names = [ore[3] for ore in feature_ores]
  feature_names.append("iceberg")

  with open("./python/data/block_ids.json", "r") as file:
    data = json.load(file)
    block_ids = data

  for block_id in block_ids:
    block_namespace = block_id.split(":")[0]
    block_path = block_id.split(":")[1]

    dimension_id = f"{block_namespace}__{block_path}"

    generate_dimension(dimension_id)
    generate_noise_settings(dimension_id, block_id)
    generate_immersive_portal(dimension_id, block_id, block_ids)

    feature_block_ids.append(block_id)

  for ore in feature_ores:
    generate_ore(feature_block_ids, ore)

  generate_biome(feature_names)

  print(f"Generated {written_files} files.")

if __name__ == "__main__":
  main()
