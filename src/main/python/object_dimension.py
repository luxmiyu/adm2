def get(dimension_id):
  dimension = {
    "type": "adm2:common",
    "generator": {
      "type": "minecraft:noise",
      "settings": f"adm2:{dimension_id}",
      "biome_source": {
        "type": "minecraft:fixed",
        "biome": "adm2:common"
      }
    }
  }

  return dimension
