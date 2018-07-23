package com.lsj.aiture;

/**
 * Created by kyyet on 2018-07-22.
 */

public class GraphNameBox {
    
        private int nameboxColor = 0xaaF44336;
        private int nameboxMarginTop = 100;
        private int nameboxMarginRight = 100;
        private int nameboxPadding = 40;
        private int nameboxTextSize = 40;
        private int nameboxTextColor = -16777216;
        private int nameboxIconWidth = 30;
        private int nameboxIconHeight = 10;
        private int nameboxTextIconMargin = 10;
        private int nameboxIconMargin = 10;

        public GraphNameBox() {
        }

        public GraphNameBox(int nameboxColor, int nameboxMarginTop, int nameboxMarginRight, int nameboxPadding, int nameboxTextSize, int nameboxTextColor, int nameboxIconWidth, int nameboxIconHeight, int nameboxTextIconMargin, int nameboxIconMargin) {
            this.nameboxColor = nameboxColor;
            this.nameboxMarginTop = nameboxMarginTop;
            this.nameboxMarginRight = nameboxMarginRight;
            this.nameboxPadding = nameboxPadding;
            this.nameboxTextSize = nameboxTextSize;
            this.nameboxTextColor = nameboxTextColor;
            this.nameboxIconWidth = nameboxIconWidth;
            this.nameboxIconHeight = nameboxIconHeight;
            this.nameboxTextIconMargin = nameboxTextIconMargin;
            this.nameboxIconMargin = nameboxIconMargin;
        }

        public int getNameboxColor() {
            return this.nameboxColor;
        }

        public void setNameboxColor(int nameboxColor) {
            this.nameboxColor = nameboxColor;
        }

        public int getNameboxIconWidth() {
            return this.nameboxIconWidth;
        }

        public void setNameboxIconWidth(int nameboxIconWidth) {
            this.nameboxIconWidth = nameboxIconWidth;
        }

        public int getNameboxIconHeight() {
            return this.nameboxIconHeight;
        }

        public void setNameboxIconHeight(int nameboxIconHeight) {
            this.nameboxIconHeight = nameboxIconHeight;
        }

        public int getNameboxTextSize() {
            return this.nameboxTextSize;
        }

        public void setNameboxTextSize(int nameboxTextSize) {
            this.nameboxTextSize = nameboxTextSize;
        }

        public int getNameboxTextColor() {
            return this.nameboxTextColor;
        }

        public void setNameboxTextColor(int nameboxTextColor) {
            this.nameboxTextColor = nameboxTextColor;
        }

        public int getNameboxMarginTop() {
            return this.nameboxMarginTop;
        }

        public void setNameboxMarginTop(int nameboxMarginTop) {
            this.nameboxMarginTop = nameboxMarginTop;
        }

        public int getNameboxMarginRight() {
            return this.nameboxMarginRight;
        }

        public void setNameboxMarginRight(int nameboxMarginRight) {
            this.nameboxMarginRight = nameboxMarginRight;
        }

        public int getNameboxPadding() {
            return this.nameboxPadding;
        }

        public void setNameboxPadding(int nameboxPadding) {
            this.nameboxPadding = nameboxPadding;
        }

        public int getNameboxTextIconMargin() {
            return this.nameboxTextIconMargin;
        }

        public void setNameboxTextIconMargin(int nameboxTextIconMargin) {
            this.nameboxTextIconMargin = nameboxTextIconMargin;
        }

        public int getNameboxIconMargin() {
            return this.nameboxIconMargin;
        }

        public void setNameboxIconMargin(int nameboxIconMargin) {
            this.nameboxIconMargin = nameboxIconMargin;
        }

}
