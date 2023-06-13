package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class JumpAndThink3 extends ApplicationAdapter {
    private static final float MOVE_VELOCITY = 200.0f;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture playerTexture;
    private Sprite playerSprite;
    private static final float GRAVITY = -1.0f;
    private boolean isJumping = false;
    private float velocityY = 0;

    private Texture backgroundTexture;
    private Rectangle backgroundRect;

    private static final int SCREEN_WIDTH = 720;
    private static final int SCREEN_HEIGHT = 480;
    private static final int WORLD_WIDTH = 2560;
    private static final int WORLD_HEIGHT = 480;

    Texture backTexture1;
    Texture backTexture2;

    OrthographicCamera hudCamera;

    private ImageButton leftButton, rightButton, jumpButton;
    private Stage stage;
    private boolean isMovingLeft;
    private boolean isMovingRight;

    private Sprite floorSprite;
    private Texture floorTexture;
    private Sprite floorSprite2;
    private Texture floorTexture2;
    private Sprite floorSprite3;
    private Texture floorTexture3;
    private int speed;
    private int speed2;
    private int speed3;



    @Override
    public void create () {
        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);
        camera.update();
        // Создание заднего фона
        batch = new SpriteBatch();
        backgroundTexture = new Texture("background3.png");
        backgroundRect = new Rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        // Создание персонажа
        playerTexture = new Texture("player.png");
        playerSprite = new Sprite(playerTexture);
        playerSprite.setPosition(30, 79);
        // Создание передвижной плиты
        floorTexture = new Texture("floor.png");
        floorSprite = new Sprite(floorTexture);
        floorSprite.setPosition(220, 79);
        // Создание передвижной плиты
        floorTexture2 = new Texture("floor.png");
        floorSprite2 = new Sprite(floorTexture2);
        floorSprite2.setPosition(1420, 82);
        // Создание передвижной плиты
        floorTexture3 = new Texture("floor.png");
        floorSprite3 = new Sprite(floorTexture3);
        floorSprite3.setPosition(1620, 79);

        backTexture1 = new Texture("back.png");
        backTexture2 = new Texture("back.png");

        // Создание камеры, перемещающейся за персонажем
        hudCamera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        hudCamera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);
        hudCamera.update();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        TextureRegionDrawable leftDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("leftBtn.png")));
        TextureRegionDrawable rightDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("rightBtn.png")));
        TextureRegionDrawable jumpDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("jumpBtn.png")));

        leftButton = new ImageButton(leftDrawable);
        leftButton.setPosition(50, 50);
        // Обработка кнопки "Влево"
        leftButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isMovingLeft = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isMovingLeft = false;
            }
        });

        rightButton = new ImageButton(rightDrawable);
        rightButton.setPosition(leftButton.getWidth() + 100, 50);
        // Обработка кнопки "Вправо"
        rightButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isMovingRight = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isMovingRight = false;
            }
        });

        jumpButton = new ImageButton(jumpDrawable);
        jumpButton.setPosition(Gdx.graphics.getWidth() - jumpButton.getWidth(), 50);
        // Обработка кнопки "Прыжок"
        jumpButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!isJumping) {
                    isJumping = true;
                    velocityY = 50.0f;
                }
            }
        });
        stage.addActor(leftButton);
        stage.addActor(rightButton);
        stage.addActor(jumpButton);
    }
    // Метод для проверки столкновения между предметами
    static boolean collision(float x, float y, float w, float h, float x1, float y1, float w1, float h1)
    {
        if (x < x1 && x + w < x1) return false;
        if (x1 < x && x1 + w1 < x) return false;
        if (y < y1 && y + h < y1) return false;
        if (y1 < y && y1 + h1 < y) return false;
        return true;
    }
    // Метод отображения предметов на экране
    @Override
    public void render () {
        // перемещение платформ туда-обратно
        if(floorSprite.getX()<=220){
            speed = 1;
        }
        if (floorSprite.getX()+floorSprite.getWidth()>=820){
            speed = -1;
        }
        floorSprite.translateX(speed);

        if (floorSprite2.getX()+floorSprite2.getWidth()>=1420){
            speed2 = -1;
        }
        if(floorSprite2.getX()<=820+floorSprite2.getWidth()){
            speed2 = 1;
        }
        floorSprite2.translateX(speed2);

        if(floorSprite3.getX()<=1620){
            speed3 = 1;
        }
        if (floorSprite3.getX()+floorSprite3.getWidth()>=2220){
            speed3 = -1;
        }
        floorSprite3.translateX(speed3);

        // Проверка столкновения персонажа с предметами
        if (playerSprite.getX() < 5) {
            playerSprite.setX(5);
        }
        if (playerSprite.getX() + playerSprite.getWidth() > WORLD_WIDTH) {
            playerSprite.setX(WORLD_WIDTH - playerSprite.getWidth());
        }
        if (playerSprite.getY() < 5) {
            playerSprite.setY(5);
        }
        if (playerSprite.getY() + playerSprite.getHeight() > WORLD_HEIGHT-5) {
            playerSprite.setY(WORLD_HEIGHT - (playerSprite.getHeight()+5));
        }

        if(playerSprite.getY()<= floorSprite.getY()+floorSprite.getHeight() && playerSprite.getY()>=6 && playerSprite.getX()+playerSprite.getWidth()>=floorSprite.getX() && playerSprite.getX()<=floorSprite.getX()+floorSprite.getWidth()){
            playerSprite.setY(floorSprite.getY()+floorSprite.getHeight());
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= floorSprite.getX() && playerSprite.getX()+playerSprite.getWidth()<=floorSprite.getX()+floorSprite.getWidth() && playerSprite.getY()<=floorSprite.getY()-5){
            playerSprite.setX(floorSprite.getX()-playerSprite.getWidth());
        }
        if(playerSprite.getX() <= floorSprite.getX()+floorSprite.getWidth() && playerSprite.getX()>=floorSprite.getX() && playerSprite.getY()<=floorSprite.getY()-5){
            playerSprite.setX(floorSprite.getX()+floorSprite.getWidth());
        }

        if(playerSprite.getY()<= floorSprite2.getY()+floorSprite2.getHeight() && playerSprite.getY()>=6 && playerSprite.getX()+playerSprite.getWidth()>=floorSprite2.getX() && playerSprite.getX()<=floorSprite2.getX()+floorSprite2.getWidth()){
            playerSprite.setY(floorSprite2.getY()+floorSprite2.getHeight());
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= floorSprite2.getX() && playerSprite.getX()+playerSprite.getWidth()<=floorSprite2.getX()+floorSprite2.getWidth() && playerSprite.getY()<=floorSprite2.getY()-5){
            playerSprite.setX(floorSprite2.getX()-playerSprite.getWidth());
        }
        if(playerSprite.getX() <= floorSprite2.getX()+floorSprite2.getWidth() && playerSprite.getX()>=floorSprite2.getX() && playerSprite.getY()<=floorSprite2.getY()-5){
            playerSprite.setX(floorSprite2.getX()+floorSprite2.getWidth());
        }

        if(playerSprite.getY()<= floorSprite3.getY()+floorSprite3.getHeight() && playerSprite.getY()>=6 && playerSprite.getX()+playerSprite.getWidth()>=floorSprite3.getX() && playerSprite.getX()<=floorSprite3.getX()+floorSprite3.getWidth()){
            playerSprite.setY(floorSprite3.getY()+floorSprite3.getHeight());
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= floorSprite3.getX() && playerSprite.getX()+playerSprite.getWidth()<=floorSprite3.getX()+floorSprite3.getWidth() && playerSprite.getY()<=floorSprite3.getY()-5){
            playerSprite.setX(floorSprite3.getX()-playerSprite.getWidth());
        }
        if(playerSprite.getX() <= floorSprite3.getX()+floorSprite3.getWidth() && playerSprite.getX()>=floorSprite3.getX() && playerSprite.getY()<=floorSprite3.getY()-5){
            playerSprite.setX(floorSprite3.getX()+floorSprite3.getWidth());
        }



        if(playerSprite.getY()<= 79 && playerSprite.getY()>=69 && playerSprite.getX()+playerSprite.getWidth()>=5 && playerSprite.getX()<=5+157){
            playerSprite.setY(79);
        }
        if(playerSprite.getX() <= 5 + 157 && playerSprite.getX()>=5 && playerSprite.getY()<=75) {
            playerSprite.setX(5 + 157);
        }

        if(playerSprite.getY()<= 130 && playerSprite.getY()>=120 && playerSprite.getX()+playerSprite.getWidth()>=2323 && playerSprite.getX()<=2323+233){
            playerSprite.setY(130);
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= 2323 && playerSprite.getX()+playerSprite.getWidth()<=2323+233 && playerSprite.getY()<=126){
            playerSprite.setX(2323-playerSprite.getWidth());
        }


        // Проверка столкновения персонажа и лавы

        if(playerSprite.getY()<= 16 && playerSprite.getX()>5+157 && playerSprite.getX()+playerSprite.getWidth()<=2323){
            playerSprite.setPosition(30, 79);
        }

        camera.position.x = playerSprite.getX();
        camera.update();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        // Рисование предметов на определенных координатах
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(backTexture1, -500,0,500,480);
        batch.draw(backTexture2, 2561,0,500,480);
        playerSprite.draw(batch);
        floorSprite.draw(batch);
        floorSprite2.draw(batch);
        floorSprite3.draw(batch);
        batch.end();

        stage.act();
        stage.draw();
        // Обработка прыжка
        if (isJumping) {
            velocityY += GRAVITY;
            playerSprite.translateY(velocityY * Gdx.graphics.getDeltaTime());
            if (playerSprite.getY() <= 5) {
                playerSprite.setY(5);
                isJumping = false;
                velocityY = 0;
            }
            else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    5,0,157,79)){
                playerSprite.setY(79);
                isJumping = false;
                velocityY = 0;
            }else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    2323,0,233,130)){
                playerSprite.setY(130);
                isJumping = false;
                velocityY = 0;
            }else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    floorSprite.getX(), floorSprite.getY(), floorSprite.getWidth(), floorSprite.getHeight())){
                playerSprite.setY(floorSprite.getY()+floorSprite.getHeight());
                isJumping = false;
                velocityY = 0;
            }else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    floorSprite2.getX(), floorSprite2.getY(), floorSprite2.getWidth(), floorSprite2.getHeight())){
                playerSprite.setY(floorSprite2.getY()+floorSprite2.getHeight());
                isJumping = false;
                velocityY = 0;
            }else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    floorSprite3.getX(), floorSprite3.getY(), floorSprite3.getWidth(), floorSprite3.getHeight())){
                playerSprite.setY(floorSprite3.getY()+floorSprite3.getHeight());
                isJumping = false;
                velocityY = 0;
            }
        }
        velocityY += GRAVITY * Gdx.graphics.getDeltaTime();
        playerSprite.translateY(velocityY * Gdx.graphics.getDeltaTime());


        if (isMovingLeft) {
            playerSprite.translateX(-MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
        } if (isMovingRight) {
            playerSprite.translateX(MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
        }


        if (playerSprite.getX() < 5) {
            playerSprite.setX(5);
        }
        if (playerSprite.getX() + playerSprite.getWidth() > WORLD_WIDTH) {
            playerSprite.setX(WORLD_WIDTH - playerSprite.getWidth());
        }

        camera.update();
        batch.setProjectionMatrix(hudCamera.combined);

    }
    @Override
    public void resize(int width, int height) {
        float aspectRatio = (float) height / (float) width;
        camera.viewportWidth = SCREEN_HEIGHT / aspectRatio;
        camera.update();
    }

    @Override
    public void dispose () {
        batch.dispose();
        playerTexture.dispose();
        backTexture1.dispose();
        backTexture2.dispose();
        floorTexture.dispose();
        floorTexture2.dispose();
        floorTexture3.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}
