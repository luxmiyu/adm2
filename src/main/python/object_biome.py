def get(ids):
  features = []

  for id in ids:
    features.append([f"adm2:{id}"])

  biome_object = {
    "temperature": 1,
    "downfall": 0.5,
    "has_precipitation": False,
    "temperature_modifier": "none",
    "creature_spawn_probability": 0,
    "effects": {
      "sky_color": 13421772,
      "fog_color": 13421772,
      "water_color": 13421772,
      "water_fog_color": 13421772
    },
    "spawners": {},
    "spawn_costs": {},
    "carvers": {
      "air": [
        "adm2:caves",
        "adm2:canyons"
      ],
      "liquid": []
    },
    "features": features
  }

  return biome_object
