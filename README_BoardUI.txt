Board UI (Pro+)
Added 3 UI upgrades:
1) Type icons: GO/JAIL/CHANCE/TAX (vector icons, no image files)
2) Corner styling + special backgrounds (corners, chance, tax, jail)
3) Houses/Hotel markers for properties (TileDTO.houses/hotels)

Note: houses/hotels are currently 0 unless you add build logic later.

Build Logic:
- Added BUILD command for HOUSE/HOTEL using your tiles.structures.Home/Hotel classes.
- TileDTO now carries houses/hotels and rent updates accordingly.

Build rule update:
- Build House/Hotel is allowed only if the player owns the full color group (Monopoly rule).

Reports added:
- Top-K report uses your custom heap (dataStructures.heap.MaxHeap).
- BST report lists properties <= price using dataStructures.bst.PropertyBST.
