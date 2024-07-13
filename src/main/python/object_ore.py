def target_object(block_id, ore_id):
  target_object = {
    "target": {
      "predicate_type": "minecraft:block_match",
      "block": f"{block_id}"
    },
    "state": {
      "Name": f"{ore_id}"
    }
  }

  return target_object

def get(block_ids, ore_id, size, count):
  targets = []

  for block_id in block_ids:
    targets.append(target_object(block_id, ore_id))

  ore_object = {
    "feature": {
      "type": "minecraft:ore",
      "config": {
        "size": size,
        "discard_chance_on_air_exposure": 0,
        "targets": targets
      }
    },
    "placement": [
      {
        "type": "minecraft:count",
        "count": count
      },
      {
        "type": "minecraft:in_square"
      },
      {
        "type": "minecraft:height_range",
        "height": {
          "type": "minecraft:uniform",
          "min_inclusive": {
            "above_bottom": 0
          },
          "max_inclusive": {
            "below_top": 0
          }
        }
      },
      {
        "type": "minecraft:biome"
      }
    ]
  }

  return ore_object
