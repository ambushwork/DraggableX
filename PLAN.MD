### STEP 1 : Make Linear RecyclerView Draggable work
1. Long click on a item, changes the color to indicate it becomes draggable.

    1.1 Make item react for long click event.

    1.2 Make recyclerview stay put after long click move.

    1.3 Change the color when long clicking, restore it when releasing.

    1.4 Make sure that the ACTION_DOWN will not be always intercepted by the item.

    1.5 Long click trigger the item decortaion
    -----------------------------------------------------------------

    1.6 Release click will remove the item decoration.

    1.7 Item decoration can move with the motion event.

2. The draggable item can be moved with the touch.
3. The draggable can affect the rest item of the list.