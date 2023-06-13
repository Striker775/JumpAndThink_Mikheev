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
public class JumpAndThink2 extends ApplicationAdapter {
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



    @Override
    public void create () {
        camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);
        camera.update();
        // Создание заднего фона
        batch = new SpriteBatch();
        backgroundTexture = new Texture("background2.png");
        backgroundRect = new Rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        // Создание персонажа
        playerTexture = new Texture("player.png");
        playerSprite = new Sprite(playerTexture);
        playerSprite.setPosition(30, 5);

        backTexture1 = new Texture("back.png");
        backTexture2 = new Texture("back.png");

        // Создание камеры, перемещающейся за персонажем
        hudCamera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        hudCamera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);
        hudCamera.update();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        // Добавление кнопок для перемещения влево, вправо и наверх
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

        // Проверка столкновения персонажа с другими предметами

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



        if(playerSprite.getY()<= 61 && playerSprite.getY()>=51 && playerSprite.getX()+playerSprite.getWidth()>=109 && playerSprite.getX()<=109+246){
            playerSprite.setY(61);
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= 109 && playerSprite.getX()+playerSprite.getWidth()<=109+246 && playerSprite.getY()<=57){
            playerSprite.setX(109-playerSprite.getWidth());
        }
        if(playerSprite.getX() <= 109+246 && playerSprite.getX()>=109 && playerSprite.getY()<=57) {
            playerSprite.setX(109 + 246);
        }

        if(playerSprite.getY()<= 125 && playerSprite.getY()>=115 && playerSprite.getX()+playerSprite.getWidth()>=447 && playerSprite.getX()<=447+298){
            playerSprite.setY(125);
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= 447 && playerSprite.getX()+playerSprite.getWidth()<=447+298 && playerSprite.getY()<=121){
            playerSprite.setX(447-playerSprite.getWidth());
        }
        if(playerSprite.getX() <= 447+298 && playerSprite.getX()>=447 && playerSprite.getY()<=121) {
            playerSprite.setX(447 + 298);
        }

        if(playerSprite.getY()<= 125 && playerSprite.getY()>=115 && playerSprite.getX()+playerSprite.getWidth()>=903 && playerSprite.getX()<=903+192){
            playerSprite.setY(125);
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= 903 && playerSprite.getX()+playerSprite.getWidth()<=903+192 && playerSprite.getY()<=121){
            playerSprite.setX(903-playerSprite.getWidth());
        }

        if(playerSprite.getY()<= 177 && playerSprite.getY()>=167 && playerSprite.getX()+playerSprite.getWidth()>=1096 && playerSprite.getX()<=1096+192){
            playerSprite.setY(177);
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= 1096 && playerSprite.getX()+playerSprite.getWidth()<=1096+192 && playerSprite.getY()<=173){
            playerSprite.setX(1096-playerSprite.getWidth());
        }
        if(playerSprite.getX() <= 1096+192 && playerSprite.getX()>=1096 && playerSprite.getY()<=173) {
            playerSprite.setX(1096 + 192);
        }

        if(playerSprite.getY()<= 83 && playerSprite.getY()>=73 && playerSprite.getX()+playerSprite.getWidth()>=1544 && playerSprite.getX()<=1544+410){
            playerSprite.setY(83);
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= 1544 && playerSprite.getX()+playerSprite.getWidth()<=1544+410 && playerSprite.getY()<=79){
            playerSprite.setX(1544-playerSprite.getWidth());
        }
        if(playerSprite.getX() <= 1544+410 && playerSprite.getX()>=1544 && playerSprite.getY()<=79) {
            playerSprite.setX(1544 + 410);
        }

        if(playerSprite.getY()<= 129 && playerSprite.getY()>=119 && playerSprite.getX()+playerSprite.getWidth()>=2060 && playerSprite.getX()<=2060+496){
            playerSprite.setY(129);
        }
        if (playerSprite.getX()+playerSprite.getWidth() >= 2060 && playerSprite.getX()+playerSprite.getWidth()<=2060+496 && playerSprite.getY()<=125){
            playerSprite.setX(2060-playerSprite.getWidth());
        }
        if(playerSprite.getX() <= 2060+496 && playerSprite.getX()>=2060 && playerSprite.getY()<=125) {
            playerSprite.setX(2060 + 496);
        }


        // Проверка столкновения персонажа и лавы
        if(playerSprite.getY()<= 16 && playerSprite.getX()>=109+246 && playerSprite.getX()+playerSprite.getWidth()<=447){
            playerSprite.setPosition(30, 5);
        }
        if(playerSprite.getY()<= 16 && playerSprite.getX()>=447+298 && playerSprite.getX()+playerSprite.getWidth()<=903){
            playerSprite.setPosition(30, 5);
        }
        if(playerSprite.getY()<= 16 && playerSprite.getX()>=1096+192 && playerSprite.getX()+playerSprite.getWidth()<=1544){
            playerSprite.setPosition(30, 5);
        }
        if(playerSprite.getY()<= 16 && playerSprite.getX()>=1544+410 && playerSprite.getX()+playerSprite.getWidth()<=2060){
            playerSprite.setPosition(30, 5);
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
                    109,0,246,61)){
                playerSprite.setY(61);
                isJumping = false;
                velocityY = 0;
            }else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    447,0,298,125)){
                playerSprite.setY(125);
                isJumping = false;
                velocityY = 0;
            }else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    903,0,192,125)){
                playerSprite.setY(125);
                isJumping = false;
                velocityY = 0;
            }else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    1096,0,192,177)){
                playerSprite.setY(177);
                isJumping = false;
                velocityY = 0;
            }else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    1544,0,410,83)){
                playerSprite.setY(83);
                isJumping = false;
                velocityY = 0;
            }else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
                    2060,0,496,129)){
                playerSprite.setY(129);
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

        // Проверка столкновения
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
        backgroundTexture.dispose();
        stage.dispose();
    }
}