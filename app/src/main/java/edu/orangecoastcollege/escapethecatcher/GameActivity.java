package edu.orangecoastcollege.escapethecatcher;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;

    //FLING THRESHOLD VELOCITY
    final int FLING_THRESHOLD = 500;

    //BOARD INFORMATION
    final int SQUARE = 200;
    final int OFFSET = 5;
    final int COLS = 8;
    final int ROWS = 8;
    final int gameBoard[][] = {
            {1, 3, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 1, 2, 2, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 2, 1, 1},
            {1, 2, 2, 1, 2, 2, 2, 1},
            {1, 2, 1, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };
    private List<ImageView> allGameObjects;
    private Player player;
    private Zombie zombie;

    //LAYOUT AND INTERACTIVE INFORMATION
    private RelativeLayout activityGameRelativeLayout;
    private ImageView zombieImageView;
    private ImageView playerImageView;
    private TextView winsTextView;
    private TextView lossesTextView;

    private int exitRow;
    private int exitCol;

    //  WINS AND LOSSES
    private int wins;
    private int losses;

    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        activityGameRelativeLayout = (RelativeLayout) findViewById(R.id.activity_game);
        winsTextView = (TextView) findViewById(R.id.winsTextView);
        lossesTextView = (TextView) findViewById(R.id.lossesTextView);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        allGameObjects = new ArrayList<>();

        // Instantiate our gesture detector
        gestureDetector = new GestureDetector(this, this);

        startNewGame();

    }

    private void startNewGame() {
        //TASK 1:  CLEAR THE BOARD (ALL IMAGE VIEWS)
        for(ImageView iv : allGameObjects)
        {
            activityGameRelativeLayout.removeView(iv);
        }

        /*
        for (int i = 0; i < allGameObjects.size(); i++) {
            ImageView visualObj = allGameObjects.get(i);
            activityGameRelativeLayout.removeView(visualObj);
        }
        */

        allGameObjects.clear();

        //TASK 2:  REBUILD THE  BOARD
        buildGameBoard();

        //TASK 3:  ADD THE PLAYERS
        createZombie();
        createPlayer();

        winsTextView.setText(getString(R.string.wins, wins));
        lossesTextView.setText(getString(R.string.losses, losses));
    }

    private void buildGameBoard() {

        // COMPLETED: Inflate the entire game board (obstacles and exit)
        // COMPLETED: (everything but the player and zombie)

        ImageView viewToInflate;

        // Loop through the board:
        for(int row = 0; row < ROWS; row++)
        {
            for(int col = 0; col < COLS; col++)
            {
                viewToInflate = null;
                if(gameBoard[row][col] == BoardCodes.OBSTACLE)
                {
                    viewToInflate = (ImageView) layoutInflater.inflate(R.layout.obstacle_layout, null);
                }
                else if(gameBoard[row][col] == BoardCodes.EXIT)
                {
                    viewToInflate = (ImageView) layoutInflater.inflate(R.layout.exit_layout, null);
                    // Assigns the exit row and column
                    exitRow = row;
                    exitCol = col;
                }
                if(viewToInflate != null)
                {
                    // Set the x and y position of the viewToInflate:
                    viewToInflate.setX(col * SQUARE + OFFSET);
                    viewToInflate.setY(row * SQUARE + OFFSET);

                    // Add the view to the relative layout and list of ImageViews
                    activityGameRelativeLayout.addView(viewToInflate);
                    allGameObjects.add(viewToInflate);
                }
            }
        }
    }

    private void createZombie() {
        // COMPLETED: Determine where to place the Zombie (at game start)
        // COMPLETED: Then, inflate the zombie layout
        int row = 2;
        int col = 4;
        zombieImageView = (ImageView) layoutInflater.inflate(R.layout.zombie_layout, null);
        zombieImageView.setX(col * SQUARE + OFFSET);
        zombieImageView.setY(row * SQUARE + OFFSET);

        // Add to relative layout and the list
        activityGameRelativeLayout.addView(zombieImageView);
        allGameObjects.add(zombieImageView);

        // Instantiate the Zombie
        zombie = new Zombie();
        zombie.setRow(row);
        zombie.setCol(col);
    }

    private void createPlayer() {
        // COMPLETED: Determine where to place the Player (at game start)
        // COMPLETED: Then, inflate the player layout
        int row = 2;
        int col = 1;

        playerImageView = (ImageView) layoutInflater.inflate(R.layout.player_layout, null);
        playerImageView.setX(col * SQUARE + OFFSET);
        playerImageView.setY(row * SQUARE + OFFSET);

        activityGameRelativeLayout.addView(playerImageView);
        allGameObjects.add(playerImageView);

        player = new Player();
        player.setRow(row);
        player.setCol(col);
    }


    private void movePlayer(float velocityX, float velocityY) {
        // COMPLETED: This method gets called by the onFling event
        // COMPLETED: Be sure to implement the move method in the Player (model) class

        int row;
        int col;

        float absX = Math.abs(velocityX);
        float absY = Math.abs(velocityY);

        String direction = "UNKNOWN";


        // x is bigger (move left or right)
        if (absX >= absY)
        {
            if (velocityX < 0)
                direction = "LEFT";
            else
                direction = "RIGHT";
        }
        else
        {
            if (velocityY < 0)
                direction = "UP";
            else
                direction = "DOWN";
        }

        if (!direction.equals("UNKNOWN"))
        {
            player.move(gameBoard, direction);

            // Move the image view as well
            playerImageView.setX(player.getCol() * SQUARE + OFFSET);
            playerImageView.setY(player.getRow() * SQUARE + OFFSET);

            // Move the zombie + ImageView
            zombie.move(gameBoard, player.getCol(), player.getRow());
            zombieImageView.setX(zombie.getCol() * SQUARE + OFFSET);
            zombieImageView.setY(zombie.getRow() * SQUARE + OFFSET);
        }

        // Make 2 decisions:
        // 1) check to see if Player has reached the exit Row and Column
        // 2) OR if the Player and Zombie are touching
        if (player.getCol() == exitCol && player.getRow() == exitRow) {

            zombieImageView = (ImageView) layoutInflater.inflate(R.layout.bunny_layout, null);
            // zombieImageView.setX(col * SQUARE + OFFSET);
            // zombieImageView.setY(row * SQUARE + OFFSET);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    wins++;
                    startNewGame();
                }
            }, 1900);

        }
        else if (player.getCol() == zombie.getCol() && player.getRow() == zombie.getRow())
        {
            playerImageView = (ImageView) layoutInflater.inflate(R.layout.blood_layout, null);
            //playerImageView.setX(col * SQUARE + OFFSET);
            //playerImageView.setY(row * SQUARE + OFFSET);
            activityGameRelativeLayout.addView(playerImageView);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    losses++;
                    startNewGame();
                }
            }, 300);
        }

        // TODO: Determine which absolute velocity is greater (x or y)
        // TODO: If x is negative, move player left.  Else if x is positive, move player right.
        // TODO: If y is negative, move player down.  Else if y is positive, move player up.

        // TODO: Then move the zombie, using the player's row and column position.
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
        movePlayer(velocityX, velocityY);
        return true;
    }
}
