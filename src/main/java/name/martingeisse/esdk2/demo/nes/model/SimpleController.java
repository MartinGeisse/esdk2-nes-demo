package name.martingeisse.esdk2.demo.nes.model;

public final class SimpleController implements Controller {

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean aPressed;
    private boolean bPressed;
    private boolean startPressed;
    private boolean selectPressed;

    @Override
    public boolean isUpPressed() {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    @Override
    public boolean isDownPressed() {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    @Override
    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    @Override
    public boolean isRightPressed() {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    @Override
    public boolean isAPressed() {
        return aPressed;
    }

    public void setAPressed(boolean aPressed) {
        this.aPressed = aPressed;
    }

    @Override
    public boolean isBPressed() {
        return bPressed;
    }

    public void setBPressed(boolean bPressed) {
        this.bPressed = bPressed;
    }

    @Override
    public boolean isStartPressed() {
        return startPressed;
    }

    public void setStartPressed(boolean startPressed) {
        this.startPressed = startPressed;
    }

    @Override
    public boolean isSelectPressed() {
        return selectPressed;
    }

    public void setSelectPressed(boolean selectPressed) {
        this.selectPressed = selectPressed;
    }

}
