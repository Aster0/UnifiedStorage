package me.astero.unifiedstoragemod.client.screen.widgets;

import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public abstract class CustomScrollWheel implements ICustomWidgetComponent {


    private static final ResourceLocation SCROLLBAR_TEXTURE
            = new ResourceLocation("container/creative_inventory/scroller");

    private static final ResourceLocation DISABLED_SCROLLBAR_TEXTURE
            = new ResourceLocation("container/creative_inventory/scroller_disabled");


    private int x, y, offsetX, offsetY, maxY, minY, pages, yPos, lastScrollPositionY;

    protected int currentPage;
    private long savedTime;


    private boolean isDragging, disabled, draggable;


    public CustomScrollWheel(int x, int y, int maxY, int pages, GridControllerMenu menu) {

        this.x = x;
        this.minY = y;
        this.y = y;
        this.maxY = maxY;
        this.pages = pages;


        currentPage = 1;
        yPos = y;
        draggable = false;

        savedTime = System.currentTimeMillis();


        if(menu.getDrawerGridControllerEntity().mergedStorageContents.size() == 0) {
            disabled = true;
        }

    }

    @Override
    public void tick(GuiGraphics guiGraphics) {


        //offsetY = (int) lerp(offsetY, targetedY, 0.5f);


        if(pages == 1 || disabled) {

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
    public void onMouseClick(double mouseX, double mouseY) {


        if(disabled && !draggable)
            return;

        Rect2i scrollbarBounds = new Rect2i(
                this.x + offsetX, yPos, 12, 15);

        if (scrollbarBounds.contains(((int) mouseX), (int) mouseY)) {


            isDragging = true;

        }

    }

    @Override
    public void onMouseDrag(double mouseX, double mouseY, int button, double dragX, double dragY) {

        if(isDragging) {

            

            double scrollPosition = mouseY - this.minY - 10;
            offsetY = this.y + (int) scrollPosition;
            yPos = offsetY;


            // Calculate the total range
            int totalRange = maxY - minY;

            // Calculate the steps per drag
            int stepsPerDrag = totalRange / pages;



            int thresholdToHit = (int) (this.minY + (currentPage) * stepsPerDrag);





            if(scrollPosition > lastScrollPositionY) { // scrolling down

                if(yPos >= thresholdToHit - stepsPerDrag / 2) {


                    if(currentPage > pages)
                        return;

                    onDragDown();
                }

            }
            else {
                if(yPos <= thresholdToHit - stepsPerDrag * 1.5 ) {


                    onDragUp();

                }

            }




            lastScrollPositionY = (int) scrollPosition;

        }

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

}
