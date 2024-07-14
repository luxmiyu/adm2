def immersive_portal_from_dimensions(block_ids = [], exclude = []):
  from_dimensions = [
    "minecraft:overworld",
    "minecraft:the_nether",
    "minecraft:the_end"
  ]

  for block_id in block_ids:
    block_namespace = block_id.split(":")[0]
    block_path = block_id.split(":")[1]
    dimension_id = f"{block_namespace}__{block_path}"

    if dimension_id not in exclude:
      from_dimensions.append(f"adm2:{dimension_id}")

  return from_dimensions

def get_to(dimension_id, block_id, block_ids):
  immersive_portal = {
    "schema_version": "imm_ptl:v1",
    "from": immersive_portal_from_dimensions(block_ids, [dimension_id]),
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