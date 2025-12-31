package model;

public class Tile {
        private int tileId;
        private TileType tileType;
        private Object tileData;

        public enum TileType{
            Go,
            PROPERTY,
            TAX,
            CARD,
            JAIL
        }
        public Tile(int tileId , TileType tileType , Object tileData){
            this.tileId = tileId;
            this.tileType = tileType;
            this.tileData = tileData;
        }
        public int getTileId() {
            return tileId;
        }
        public int getTileType() {
            return tileType.ordinal();
        }
        public Object getTileData() {
            return tileData;
        }
}

