package me.astero.unifiedstoragemod.client.screen.widgets.generic;

import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

public abstract class CustomScrollWheel implements ICustomWidgetComponent {


    private static final ResourceLocation SCROLLBAR_TEXTURE
            = new ResourceLocation("container/creative_inventory/scroller");

    private static final ResourceLocation DISABLED_SCROLLBAR_TEXTURE
            = new ResourceLocation("container/creative_inventory/scroller_disabled");


    private int x, y, offsetX, offsetY, maxY, minY, pages, yPos, lastScrollPositionY;

    protected int currentPage;
    private long savedTime;


    private boolean isDragging, disabled, draggable;


    public CustomScrollWheel(int x, int y, int maxY, int pages, StorageControllerMenu menu) {

        this.x = x;
        this.minY = y;
        this.y = y;
        this.maxY = maxY;
        this.pages = pages;


        currentPage = 1;
        yPos = y;
        draggable = false;

        savedTime = System.currentTimeMillis();


        if(menu.getStorageControllerEntity().mergedStorageContents.size() == 0) {
            disabled = true;
        }

    }

    public void setPages(int pages) {

        yPos = this.minY;
        this.pages = pages;

        if(pages > 1) {
            disabled = false;
        }

    }

    public int getPages() {
        return pages;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void tick(GuiGraphics guiGraphics, int leftPos, int topPos) {


        //offsetY = (int) lerp(offsetY, targetedY, 0.5f);



        if(pages < 2 || disabled) {

            guiGraphics.blitSprite(DISABLED_SCROLLBAR_TEXTURE, this.x + offsetX, yPos,
                    12, 15);


            disabled = true;

            return;

        }


        if(yPos > maxY) {
            yPos = maxY;
        }
        else if(yPos < minY) {
            yPos = minY;

        }



        guiGraphics.blitSprite(SCROLLBAR_TEXTURE, this.x + offsetX, yPos,
                12, 15);


        if(System.currentTimeMillis() - savedTime > 10) {
            draggable = true;
        }


    }

    @Override
    public void onMouseClick(double mouseX, double mouseY, int mode) {


        if(disabled && !draggable)
            return;

        Rect2i scrollbarBounds = new Rect2i(
                this.x + offsetX, yPos, 12, 15);

        if (scrollbarBounds.contains(((int) mouseX), (int) mouseY)) {


            isDragging = true;

        }

    }


    private int getTotalSteps() {
        // Calculate the total range
        int totalRange = maxY - minY;



        if(pages == 0) {
            return 0;
        }

        // Calculate the steps per drag
        int stepsPerDrag = totalRange / pages;

        return stepsPerDrag;
    }
    @Override
    public void onMouseDrag(double mouseX, double mouseY, int button, double dragX, double dragY) {



        if(isDragging && !disabled) {



            double scrollPosition = mouseY - this.minY - 10;
            offsetY = this.y + (int) scrollPosition;
            yPos = offsetY;





            int thresholdToHit = (int) (this.minY + (currentPage) * getTotalSteps());





            if(scrollPosition > lastScrollPositionY) { // scrolling down

                if(yPos >= thresholdToHit - getTotalSteps() / 2) {


                    onDragDown();
                }

            }
            else {
                if(yPos <= thresholdToHit - getTotalSteps() * 1.5 ) {


                    onDragUp();

                }

            }




            lastScrollPositionY = (int) scrollPosition;

        }

    }

    @Override
    public void onMouseScrolled(double mouseX, double mouseY, double delta, double rawDelta) {


        if(pages <= 1)
            return;


        if(rawDelta <= -1) { // scroll down

            forceHitThreshold(0);

            onDragDown();



            if(currentPage == pages) {
                yPos = this.maxY;
            }
        }
        else {
            onDragUp();

            forceHitThreshold(-1);



            if(currentPage == 1) {
                yPos = this.minY;
            }



        }


    }

    private void forceHitThreshold(int value) {
        int thresholdToHit = (int) (this.minY + (currentPage + value) * getTotalSteps());

        yPos = thresholdToHit;
    }

    @Override
    public void onMouseRelease() {

        isDragging = false;
    }

    private double lerp(double start, double end, double progress) {
        return start + progress * (end - start);
    }


    public abstract void onDragDown();
    public abstract void onDragUp();

    @Override
    public void renderCustomTooltip(GuiGraphics guiGraphics, Font font, int x, int y, Slot slot) {

    }

}
