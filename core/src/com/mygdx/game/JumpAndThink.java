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
public class JumpAndThink extends ApplicationAdapter {
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
	private Rectangle wall;
	private Texture wallTexture;
	private Texture nothingTexture;

	private Texture cubeTexture1;
	private Sprite cubeSprite1;

	private Texture cubeTexture2;
	private Sprite cubeSprite2;

	private Texture cubeTexture3;
	private Sprite cubeSprite3;

	private Rectangle button;
	private Rectangle button2;
	private Texture buttonTexture;

	private Sprite floorSprite;
	private Texture floorTexture;

	@Override
	public void create () {
		camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);
		camera.update();

		// Создание заднего фона
		batch = new SpriteBatch();
		backgroundTexture = new Texture("background1.png");
		backgroundRect = new Rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

		// Создание персонажа
		playerTexture = new Texture("player.png");
		playerSprite = new Sprite(playerTexture);
		playerSprite.setPosition(30, 5);

		backTexture1 = new Texture("back.png");
		backTexture2 = new Texture("back.png");
		// Создание передвижной плиты
		floorTexture = new Texture("floor.png");
		floorSprite = new Sprite(floorTexture);
		floorSprite.setPosition(1600, 60);
		// Создание передвижного куба
		cubeTexture1 = new Texture("cube.png");
		cubeSprite1 = new Sprite(cubeTexture1);
		cubeSprite1.setPosition(60,5);
		// Создание передвижного куба
		cubeTexture2 = new Texture("cube.png");
		cubeSprite2 = new Sprite(cubeTexture2);
		cubeSprite2.setPosition(670,87);
		// Создание передвижного куба
		cubeTexture3 = new Texture("cube.png");
		cubeSprite3 = new Sprite(cubeTexture3);
		cubeSprite3.setPosition(1350,87);
		// Создание кнопки
		buttonTexture = new Texture("button.png");
		button = new Rectangle(576,87,buttonTexture.getWidth(),buttonTexture.getHeight());
		// Создание кнопки
		button2 = new Rectangle(1200,87,buttonTexture.getWidth(),buttonTexture.getHeight());
		// Создание камеры, перемещающейся за персонажем
		hudCamera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
		hudCamera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);
		hudCamera.update();
		// Создание стены
		wallTexture = new Texture("wall.png");
		wall = new Rectangle(850, 0, wallTexture.getWidth(), wallTexture.getHeight());

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
		boolean collisingwithcube = false;
		// Проверка столкновения предметов с кнопкой
		if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
				button.getX(), button.getY(), button.getWidth(), button.getHeight()) ||
				collision(cubeSprite2.getX(), cubeSprite2.getY(), cubeSprite2.getWidth(), cubeSprite2.getHeight(),
						button.getX(), button.getY(), button.getWidth(), button.getHeight()))
		{
			// При столкновении стена исчезает
			wall.setX(3000);
			wallTexture = new Texture("nothing.png");
		}else{
			// Если нет столкновения - стена остаётся на месте
			wall.setX(850);
			wallTexture = new Texture("wall.png");
		}
		// Проверка столкновения предметов с кнопкой
		if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
				button2.getX(), button2.getY(), button2.getWidth(), button2.getHeight()) ||
				collision(cubeSprite3.getX(), cubeSprite3.getY(), cubeSprite3.getWidth(), cubeSprite3.getHeight(),
						button2.getX(), button2.getY(), button2.getWidth(), button2.getHeight()))
		{
			// если движущаяся платформа не достигла нужных координат, то она перемещается вправо
			if(floorSprite.getX()+floorTexture.getWidth()!=1600+530){
				floorSprite.translateX(1);
				if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
						floorSprite.getX(),floorSprite.getY(),floorSprite.getWidth(),floorSprite.getHeight())){
					playerSprite.translateX(1);
				}
			}
			// Если кнопка не нажата, то платформа движется на изначальную точку
		}else{
			if(floorSprite.getX()!=1600){
				floorSprite.translateX(-1);
				if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
						floorSprite.getX(),floorSprite.getY(),floorSprite.getWidth(),floorSprite.getHeight())){
					playerSprite.translateX(-1);
				}
			}
		}

		// Проверка столкновения кубов и персонажа с другими предметами

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

		if (cubeSprite1.getX() < 5) {
			cubeSprite1.setX(5);
		}
		if (cubeSprite1.getX() + cubeSprite1.getWidth() > WORLD_WIDTH) {
			cubeSprite1.setX(WORLD_WIDTH - cubeSprite1.getWidth());
		}
		if (cubeSprite1.getY() < 5) {
			cubeSprite1.setY(5);
		}
		if (cubeSprite1.getY() + cubeSprite1.getHeight() > WORLD_HEIGHT-5) {
			cubeSprite1.setY(WORLD_HEIGHT - (cubeSprite1.getHeight()+5));
		}
		if (cubeSprite2.getX()+cubeSprite2.getWidth() >= wall.getX() && cubeSprite2.getX()+cubeSprite2.getWidth()<= wall.getX()+wall.getWidth() || cubeSprite2.getX() >= wall.getX() && cubeSprite2.getX() <= wall.getX()+wall.getWidth()){
			cubeSprite2.setX(wall.getX()-cubeSprite2.getWidth());
		}

		if (playerSprite.getX()+playerSprite.getWidth() >= wall.getX() && playerSprite.getX()+playerSprite.getWidth()<= wall.getX()+wall.getWidth() || playerSprite.getX() >= wall.getX() && playerSprite.getX() <= wall.getX()+wall.getWidth()){
			playerSprite.setX(wall.getX()-playerSprite.getWidth());
		}
		// Передвижение куба при помощи персонажа

		if(playerSprite.getY() <= cubeSprite1.getY()+cubeSprite1.getHeight() && playerSprite.getY() != 5 &&
				((playerSprite.getX() <= cubeSprite1.getX() && playerSprite.getX()+playerSprite.getWidth() >= cubeSprite1.getX()+cubeSprite1.getWidth()) ||
						(playerSprite.getX() >= cubeSprite1.getX() && playerSprite.getX() <= cubeSprite1.getX()+cubeSprite1.getWidth()) ||
						(playerSprite.getX()+playerSprite.getWidth() >= cubeSprite1.getX() && playerSprite.getX()+playerSprite.getWidth() <= cubeSprite1.getX()+cubeSprite1.getWidth()))){
			playerSprite.setY(cubeSprite1.getY()+cubeSprite1.getHeight());
			collisingwithcube = true;
		}
		else if(playerSprite.getX()<= cubeSprite1.getX()+cubeSprite1.getWidth() && playerSprite.getX()>cubeSprite1.getX() && playerSprite.getY() <= cubeSprite1.getY()+cubeSprite1.getHeight())
		{
			playerSprite.setX(cubeSprite1.getX()+cubeSprite1.getWidth());
			cubeSprite1.translateX(-MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
			playerSprite.translateX(-(MOVE_VELOCITY/2) * Gdx.graphics.getDeltaTime());
		}
		else if(playerSprite.getX()+playerSprite.getWidth()>= cubeSprite1.getX() && playerSprite.getX()<cubeSprite1.getX()+cubeSprite1.getWidth() && playerSprite.getY() <= cubeSprite1.getY()+cubeSprite1.getHeight())
		{
			playerSprite.setX(cubeSprite1.getX()-playerSprite.getWidth());
			cubeSprite1.translateX(MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
			playerSprite.translateX((MOVE_VELOCITY/2) * Gdx.graphics.getDeltaTime());
		}


		// Передвижение куба при помощи персонажа
		if(playerSprite.getY() <= cubeSprite2.getY()+cubeSprite2.getHeight() && playerSprite.getY() >= cubeSprite2.getY()+cubeSprite2.getHeight()/2 &&
				((playerSprite.getX() <= cubeSprite2.getX() && playerSprite.getX()+playerSprite.getWidth() >= cubeSprite2.getX()+cubeSprite2.getWidth()) ||
						(playerSprite.getX() >= cubeSprite2.getX() && playerSprite.getX() <= cubeSprite2.getX()+cubeSprite2.getWidth()) ||
						(playerSprite.getX()+playerSprite.getWidth() >= cubeSprite2.getX() && playerSprite.getX()+playerSprite.getWidth() <= cubeSprite2.getX()+cubeSprite2.getWidth()))){
			playerSprite.setY(cubeSprite2.getY()+cubeSprite2.getHeight());
			collisingwithcube = true;
		}
		else if(playerSprite.getX()<= cubeSprite2.getX()+cubeSprite2.getWidth() && playerSprite.getX()>cubeSprite2.getX() && playerSprite.getY() <= cubeSprite2.getY()+cubeSprite2.getHeight())
		{
			playerSprite.setX(cubeSprite2.getX()+cubeSprite2.getWidth());
			cubeSprite2.translateX(-MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
			playerSprite.translateX(-(MOVE_VELOCITY/2) * Gdx.graphics.getDeltaTime());
		}
		else if(playerSprite.getX()+playerSprite.getWidth()>= cubeSprite2.getX() && playerSprite.getX()<cubeSprite2.getX()+cubeSprite2.getWidth() && playerSprite.getY() <= cubeSprite2.getY()+cubeSprite2.getHeight())
		{
			playerSprite.setX(cubeSprite2.getX()-playerSprite.getWidth());
			cubeSprite2.translateX(MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
			playerSprite.translateX((MOVE_VELOCITY/2) * Gdx.graphics.getDeltaTime());
		}

		// Передвижение куба при помощи персонажа
		if(playerSprite.getY() <= cubeSprite3.getY()+cubeSprite3.getHeight() && playerSprite.getY() >=cubeSprite2.getY()+cubeSprite2.getHeight()/2 &&
				((playerSprite.getX() <= cubeSprite3.getX() && playerSprite.getX()+playerSprite.getWidth() >= cubeSprite3.getX()+cubeSprite3.getWidth()) ||
						(playerSprite.getX() >= cubeSprite3.getX() && playerSprite.getX() <= cubeSprite3.getX()+cubeSprite3.getWidth()) ||
						(playerSprite.getX()+playerSprite.getWidth() >= cubeSprite3.getX() && playerSprite.getX()+playerSprite.getWidth() <= cubeSprite3.getX()+cubeSprite3.getWidth()))){
			playerSprite.setY(cubeSprite3.getY()+cubeSprite3.getHeight());
			collisingwithcube = true;
		}
		else if(playerSprite.getX()<= cubeSprite3.getX()+cubeSprite3.getWidth() && playerSprite.getX()>cubeSprite3.getX() && playerSprite.getY() <= cubeSprite3.getY()+cubeSprite3.getHeight())
		{
			playerSprite.setX(cubeSprite3.getX()+cubeSprite3.getWidth());
			cubeSprite3.translateX(-MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
			playerSprite.translateX(-(MOVE_VELOCITY/2) * Gdx.graphics.getDeltaTime());
		}
		else if(playerSprite.getX()+playerSprite.getWidth()>= cubeSprite3.getX() && playerSprite.getX()<cubeSprite3.getX()+cubeSprite3.getWidth() && playerSprite.getY() <= cubeSprite3.getY()+cubeSprite3.getHeight())
		{
			playerSprite.setX(cubeSprite3.getX()-playerSprite.getWidth());
			cubeSprite3.translateX(MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
			playerSprite.translateX((MOVE_VELOCITY/2) * Gdx.graphics.getDeltaTime());
		}

		// Проверка столкновения персонажа с платформой
		if(playerSprite.getY()<= floorSprite.getY()+floorSprite.getHeight() && playerSprite.getY()>=6 && playerSprite.getX()+playerSprite.getWidth()>=floorSprite.getX() && playerSprite.getX()<=floorSprite.getX()+floorSprite.getWidth()){
			playerSprite.setY(floorSprite.getY()+floorSprite.getHeight());
		}
		if (playerSprite.getX()+playerSprite.getWidth() >= floorSprite.getX() && playerSprite.getX()+playerSprite.getWidth()<=floorSprite.getX()+floorSprite.getWidth() && playerSprite.getY()<=floorSprite.getY()-5){
			playerSprite.setX(floorSprite.getX()-playerSprite.getWidth());
		}
		if(playerSprite.getX() <= floorSprite.getX()+floorSprite.getWidth() && playerSprite.getX()>=floorSprite.getX() && playerSprite.getY()<=floorSprite.getY()-5){
			playerSprite.setX(floorSprite.getX()+floorSprite.getWidth());
		}

		// Проверка на столкновения кубов и персонажа с задним фоном

		if(playerSprite.getY()<= 89 && playerSprite.getY()>=79 && playerSprite.getX()+playerSprite.getWidth()>=273 && playerSprite.getX()<=273+182){
			playerSprite.setY(89);
		}
		if (playerSprite.getX()+playerSprite.getWidth() >= 273 && playerSprite.getX()+playerSprite.getWidth()<=273+182 && playerSprite.getY()<=85){
			playerSprite.setX(273-playerSprite.getWidth());
		}
		if(playerSprite.getX() <= 273+182 && playerSprite.getX()>=273 && playerSprite.getY()<=85){
			playerSprite.setX(273+182);
		}
		if(cubeSprite1.getY()<= 89 && cubeSprite1.getY()>=79 && cubeSprite1.getX()+cubeSprite1.getWidth()>=273 && cubeSprite1.getX()<=273+182){
			cubeSprite1.setY(89);
		}
		if (cubeSprite1.getX()+cubeSprite1.getWidth() >= 273 && cubeSprite1.getX()+cubeSprite1.getWidth()<=273+182 && cubeSprite1.getY()<=85){
			cubeSprite1.setX(273-cubeSprite1.getWidth());
		}
		if(cubeSprite1.getX() <= 273+182 && cubeSprite1.getX()>=273 && cubeSprite1.getY()<=85){
			cubeSprite1.setX(273+182);
		}

		if(playerSprite.getY()<= 87 && playerSprite.getY()>=77 && playerSprite.getX()+playerSprite.getWidth()>=569 && playerSprite.getX()<=569+368){
			playerSprite.setY(87);
		}
		if (playerSprite.getX()+playerSprite.getWidth() >= 569 && playerSprite.getX()+playerSprite.getWidth()<=569+368 && playerSprite.getY()<=83){
			playerSprite.setX(569-playerSprite.getWidth());
		}
		if(playerSprite.getX() <= 569+368 && playerSprite.getX()>=569 && playerSprite.getY()<=83){
			playerSprite.setX(569+368);
		}
		if(cubeSprite2.getY()<= 87 && cubeSprite2.getY()>=77 && cubeSprite2.getX()+cubeSprite2.getWidth()>=569 && cubeSprite2.getX()<=569+368){
			cubeSprite2.setY(87);
		}

		if(playerSprite.getY()<= 87 && playerSprite.getY()>=77 && playerSprite.getX()+playerSprite.getWidth()>=1046 && playerSprite.getX()<=1046+473){
			playerSprite.setY(87);
		}
		if (playerSprite.getX()+playerSprite.getWidth() >= 1046 && playerSprite.getX()+playerSprite.getWidth()<=1046+473 && playerSprite.getY()<=83){
			playerSprite.setX(1046-playerSprite.getWidth());
		}
		if(playerSprite.getX() <= 1046+473 && playerSprite.getX()>=1046 && playerSprite.getY()<=83){
			playerSprite.setX(1046+473);
		}
		if(cubeSprite3.getY()<= 87 && cubeSprite3.getY()>=77 && cubeSprite3.getX()+cubeSprite3.getWidth()>=1046 && cubeSprite3.getX()<=1046+473){
			cubeSprite3.setY(87);
		}

		if(playerSprite.getY()<= 160 && playerSprite.getY()>=150 && playerSprite.getX()+playerSprite.getWidth()>=2248 && playerSprite.getX()<=2248+312){
			playerSprite.setY(160);
		}
		if (playerSprite.getX()+playerSprite.getWidth() >= 2248 && playerSprite.getX()+playerSprite.getWidth()<=2248+312 && playerSprite.getY()<=156){
			playerSprite.setX(2248-playerSprite.getWidth());
		}

		// Проверка столкновения персонажа и лавы

		if(playerSprite.getY()<= 16 && playerSprite.getX()>=273+182 && playerSprite.getX()+playerSprite.getWidth()<=569){
			playerSprite.setPosition(30, 5);
		} else if (playerSprite.getY()<= 16 && playerSprite.getX()>=569+368 && playerSprite.getX()+playerSprite.getWidth()<=1046) {
			playerSprite.setPosition(30, 5);
		} else if (playerSprite.getY()<= 16 && playerSprite.getX()>=1046+473 && playerSprite.getX()+playerSprite.getWidth()<=2248) {
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
		batch.draw(buttonTexture, 576,87, buttonTexture.getWidth(), buttonTexture.getHeight());
		batch.draw(buttonTexture, 1200, 87, buttonTexture.getWidth(), buttonTexture.getHeight());
		batch.draw(wallTexture, 850, 0, wallTexture.getWidth(), wallTexture.getHeight());
		batch.draw(backTexture1, -500,0,500,480);
		batch.draw(backTexture2, 2561,0,500,480);
		playerSprite.draw(batch);
		floorSprite.draw(batch);
		cubeSprite1.draw(batch);
		cubeSprite2.draw(batch);
		cubeSprite3.draw(batch);
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
					cubeSprite1.getX(), cubeSprite1.getY(), cubeSprite1.getWidth(), cubeSprite1.getHeight())){
				if((velocityY < 0) && playerSprite.getY()<= cubeSprite1.getY()+cubeSprite1.getHeight()){
					playerSprite.setY(cubeSprite1.getY()+cubeSprite1.getHeight());
					isJumping = false;
					velocityY = 0;
				}
			}
			else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
					273,0,182,89)){
				playerSprite.setY(89);
				isJumping = false;
				velocityY = 0;
			}else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
					569,0,368,87)){
				playerSprite.setY(87);
				isJumping = false;
				velocityY = 0;
			}else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
					1046,0,473,87)){
				playerSprite.setY(87);
				isJumping = false;
				velocityY = 0;
			}else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
					2248,0,312,160)){
				playerSprite.setY(160);
				isJumping = false;
				velocityY = 0;
			}
			else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
					cubeSprite2.getX(), cubeSprite2.getY(), cubeSprite2.getWidth(), cubeSprite2.getHeight())){
				playerSprite.setY(cubeSprite2.getY()+cubeSprite2.getHeight());
				isJumping = false;
				velocityY = 0;
			}else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
					cubeSprite3.getX(), cubeSprite3.getY(), cubeSprite3.getWidth(), cubeSprite3.getHeight())){
				playerSprite.setY(cubeSprite3.getY()+cubeSprite3.getHeight());
				isJumping = false;
				velocityY = 0;
			}else if (collision(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight(),
					floorSprite.getX(), floorSprite.getY(), floorSprite.getWidth(), floorSprite.getHeight())){
				playerSprite.setY(floorSprite.getY()+floorSprite.getHeight());
				isJumping = false;
				velocityY = 0;
			}
		}
		velocityY += GRAVITY * Gdx.graphics.getDeltaTime();
		playerSprite.translateY(velocityY * Gdx.graphics.getDeltaTime());
		// Сила тяжести кубов
		if(cubeSprite1.getY()>=5){
			cubeSprite1.translateY(-2);
		}
		if(cubeSprite2.getY()>=5){
			cubeSprite2.translateY(-2);
		}
		if(cubeSprite3.getY()>=5){
			cubeSprite3.translateY(-2);
		}
		// Передвижение персонажа
		if (isMovingLeft) {
			playerSprite.translateX(-MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
		} if (isMovingRight) {
			playerSprite.translateX(MOVE_VELOCITY * Gdx.graphics.getDeltaTime());
		}
		// Проверка столкновения
		if (playerSprite.getX() < 5) {
			playerSprite.setX(5);
		}
		if (cubeSprite1.getX() < 5) {
			cubeSprite1.setX(5);
		}
		if (cubeSprite2.getX() < 5) {
			cubeSprite2.setX(5);
		}
		if (cubeSprite3.getX() < 5) {
			cubeSprite3.setX(5);
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
		cubeTexture1.dispose();
		cubeTexture2.dispose();
		floorTexture.dispose();
		playerTexture.dispose();
		buttonTexture.dispose();
		wallTexture.dispose();
		backTexture1.dispose();
		backTexture2.dispose();
		backgroundTexture.dispose();
		stage.dispose();
	}
}