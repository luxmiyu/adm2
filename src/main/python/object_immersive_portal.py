def immersive_portal_from_dimensions(vanilla_dimensions = [], modded_dimensions = [], exclude = []):
  from_dimensions = [
    "minecraft:overworld",
    "minecraft:the_nether",
    "minecraft:the_end"
  ]
  
  for dimensionId in vanilla_dimensions:
    if f"minecraft__{dimensionId}" not in exclude:
      from_dimensions.append(f"adm2:minecraft__{dimensionId}")

  for dimension in modded_dimensions:
    dimensionId = dimension["dimensionId"]
    if dimensionId not in exclude:
      from_dimensions.append(f"adm2:{dimensionId}")

  return from_dimensions

def get_to(dimension_id, block_id, vanilla_dimensions = [], modded_dimensions = []):
  immersive_portal = {
    "schema_version": "imm_ptl:v1",
    "from": immersive_portal_from_dimensions(vanilla_dimensions, modded_dimensions, [dimension_id]),
    "to": f"adm2:{dimension_id}",
    "form": {
      "type": "imm_ptl:classical",
      "from_frame_block": f"{block_id}",
      "area_block": "minecraft:air",
      "to_frame_block": f"{block_id}",
      "generate_frame_if_not_found": True
    },
    "trigger": {
      "type": "imm_ptl:use_item",
      "item": "adm2:any_dimensional_portal_wand"
    }
  }

  return immersive_portal

def get_from(dimension_id, block_id):
  immersive_portal = {
    "schema_version": "imm_ptl:v1",
    "from": [
      f"adm2:{dimension_id}"
    ],
    "to": "minecraft:overworld",
    "form": {
      "type": "imm_ptl:classical",
      "from_frame_block": f"{block_id}",
      "area_block": "minecraft:air",
      "to_frame_block": f"{block_id}",
      "generate_frame_if_not_found": True
    },
    "trigger": {
      "type": "imm_ptl:use_item",
      "item": "adm2:any_dimensional_portal_wand"
    }
  }

  return immersive_portal