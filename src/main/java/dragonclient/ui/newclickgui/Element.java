package dragonclient.ui.newclickgui;

public class Element {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean visible;

    private int scroll;

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void drawScreen(int mouseX, int mouseY, float button, boolean last) {
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    public boolean mouseReleased(int mouseX, int mouseY, int state) {
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y - scroll;
    }

    public int getRawY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }

    public int getScroll() {
        return scroll;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    protected int hoverColor(int color, int hover) {
        int r = (color >> 16 & 0xFF) - hover * 2;
        int g = (color >> 8 & 0xFF) - hover * 2;
        int b = (color & 0xFF) - hover * 2;
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }
}
