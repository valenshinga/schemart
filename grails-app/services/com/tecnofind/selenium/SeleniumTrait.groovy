package com.tecnofind.selenium

import com.tecnofind.Auxiliar
import com.tecnofind.User
import com.tecnofind.ProxyTecnofind
import com.tecnofind.ProxyTecnofindService

import grails.util.Holders
import groovyx.net.http.HTTPBuilder
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.apache.commons.io.FileUtils
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.openqa.selenium.*
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.LocalFileDetector
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.UselessFileDetector
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

trait SeleniumTrait {
	
	def proxyTecnofindService
	//def grailsApplication : tiene que estar declarado en el service donde se use

	private static final boolean printearMensajes = Auxiliar.testingEnviroment

	public WebDriver inicializarDriver(Boolean remoto = true, Boolean necesitoProxy = false) throws AssertionError{
		Map<String, Object> prefsMap = new HashMap<String, Object>()
		prefsMap.put("profile.default_content_settings.popups", 0)
		prefsMap.put("download.prompt_for_download", "false")
		prefsMap.put("download.directory_upgrade", "true")
		prefsMap.put("safebrowsing.enabled", "true")
		prefsMap.put("plugins.always_open_pdf_externally", true)
		prefsMap.put("plugins.plugins_disabled", "Chrome PDF Viewer")

		ChromeOptions options = new ChromeOptions()
		options.addArguments("--window-size=1400,900")
		options.setExperimentalOption("prefs", prefsMap)
		options.addArguments("--test-type")
		if (Auxiliar.testingEnviroment || remoto)
			System.setProperty("webdriver.chrome.driver","../chromedriver_linux64/chromedriver");
		else{
			System.setProperty("webdriver.chrome.driver","/usr/bin/chromedriver");
			options.addArguments("--headless")
		}
		options.addArguments('--kiosk-printing')
		System.setProperty("webdriver.chrome.args", "--disable-logging");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		options.addArguments("--disable-extensions")
		def driver
		if (necesitoProxy) { 
			List<String> userAgents = [
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/602.3.12 (KHTML, like Gecko) Version/10.0 Safari/602.3.12",
				"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36"
			]
			String randomUserAgent = userAgents[new Random().nextInt(userAgents.size())]
			options.addArguments("user-agent=" + randomUserAgent)
			// Configurar Proxy
			String proxyTecnofind = proxyTecnofindService.obtenerProxyAleatorio()?.toString()
			Proxy proxySelenium = new Proxy()
			proxySelenium.setHttpProxy(proxyTecnofind)
			proxySelenium.setSslProxy(proxyTecnofind)
			options.setProxy(proxySelenium)
		}

		if (remoto){
			options.addArguments("--safebrowsing.enabled")
			options.addArguments("--disable-download-notification")
			def capabilities = DesiredCapabilities.chrome()
			capabilities.setCapability(ChromeOptions.CAPABILITY, options)
			try {
				driver = new RemoteWebDriver(new URL("http://localhost:4444"), capabilities)
			}
			catch(Exception e) {
				if(!Auxiliar.testingEnviroment)
					println "No se inició el túnel: $e.message"
				return inicializarDriver(false, necesitoProxy)
				// assert false: "Túnel cerradofinerror"
			}
			driver.metaClass.timeoutRemoto = 3
		}else
			driver = new ChromeDriver(options)
		driver.metaClass.remoto = remoto
		driver.metaClass.moverCamara = true
		driver.metaClass.javascriptExecutor = ((JavascriptExecutor) driver)
		return driver
	}

	def cambiarPestaña(WebDriver driver, Integer numero = 2, Integer timeout = 15){
		if (printearMensajes)
			println "Intentando cambiar a la pestaña $numero del WebDriver "
		numero-- // la ventana 2 es la posición 1 del array
		while (timeout){
			timeout--
			def ventanas = driver.getWindowHandles()
			try {
				driver.switchTo().window(ventanas[numero]);
				break
			}
			catch(Exception e) {
				Thread.sleep(1000)
			}
		}
		driver.switchTo().window((driver.getWindowHandles())[numero]);
	}

	public WebElement buscar(padre, String xpathOrId, Long timeout = 5, Boolean moverCamara = null, Boolean javascript = false){
		def elemento;
		if (printearMensajes)
			println "buscando elemento..... " + xpathOrId
		if (xpathOrId =~ "[\\[]|//"){
			if (padre instanceof WebDriver && !javascript)
				elemento = new WebDriverWait(padre, timeout).until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOrId)))
			else
				elemento = padre.findElement(By.xpath(xpathOrId))
		} else {
			if (padre instanceof WebDriver && !javascript)
				elemento = new WebDriverWait(padre, timeout).until(ExpectedConditions.elementToBeClickable(By.id(xpathOrId)))
			else
				elemento = padre.findElement(By.id(xpathOrId))
		}
		try {
			if (moverCamara == true || (moverCamara == null && padre instanceof WebDriver && padre.moverCamara))
				padre.javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", elemento);
				padre.javascriptExecutor.executeScript("window.scrollBy(0, -100);");
		}
		catch(Exception e) {
		}

		if (padre instanceof WebDriver && padre.remoto){ // pintar
			try {
				String originalStyle = elemento.getAttribute("style");
				padre.javascriptExecutor.executeScript("arguments[0].setAttribute('style', arguments[1] + 'border:2px solid red; background:yellow;');", elemento, originalStyle);
				// Thread.sleep(100); // Resaltar por un breve período
				padre.javascriptExecutor.executeScript("arguments[0].setAttribute('style', arguments[1]);", elemento, originalStyle);
			} catch(Exception e) {
				// Manejar la excepción si es necesario
			}
		}
		elemento.metaClass.refrescarInstancia = {buscar(padre, xpathOrId, timeout, moverCamara, javascript)}
		return elemento
	}

	public void opcionDeCombo(WebDriver driver, String combo, opcion){
		buscar(driver, combo).with{
			click()
			findElements(By.tagName("option")).find{
				try {
					def consultada = opcion instanceof Integer ? new Integer(it.getAttribute("value")) : it.getAttribute("value")
					return consultada == opcion
				}
				catch(Exception e) {
					return false
				}
			}.click()
		}
	}

	public boolean explorarFrames(WebDriver driver, String xpath, Boolean javascript = false, camino = null) { // SOLO USAR EN TESTING
		if (!camino){
			camino = []
			driver.switchTo().defaultContent()
		}
		println "Entrada a explorarFrames con camino $camino"
		try {
			clickear(driver, xpath, timeout: 1, javascript: javascript)
			Auxiliar.separador("Elemento encontrado en el camino de frames: $camino")
			return true
		} catch (Exception e) {
			if (e.message.contains("Expected condition failed: waiting for element to be clickable"))
				println "Expected condition failed: waiting for element to be clickable"
			else if (e.message.contains("no such element: Unable to locate element"))
				println "no such element: Unable to locate element"
			else
				throw e
		}
		int frame = 0
		try {
			while(true){
				cambiarFrame(driver, frame)
				def nuevoCamino = camino.collect()
				nuevoCamino << frame
				if (explorarFrames(driver, xpath, javascript, nuevoCamino))
					return true
				cambiarFrame(driver, -1)
				frame++
			}
		}
		catch(Exception e) {
			if (e.message.contains("no such frame"))
				println "No existe frame $frame para agregar al camino $camino"
			else
				Auxiliar.printearError(e, "Error cambiando frame")
		}
		return false
	}

	public WebElement clickear(Map params, padre, String xpathOrId){
		// def padre = params.get('padre')
		// String xpathOrId = params.get('xpathOrId')
		Long timeout = params.get('timeout', 10)
		Long retrasoRandom = params.get('retrasoRandom', 0)
		Boolean moverCamara = params.get('moverCamara', null)
		Boolean javascript = params.get('javascript', false)
		return clickear(padre, xpathOrId, timeout, retrasoRandom, moverCamara, javascript)
	}
	public WebElement clickear(padre, String xpathOrId, Long timeout = 10, Long retrasoRandom = 0, Boolean moverCamara = null, Boolean javascript = false){
		if (retrasoRandom)
			Thread.sleep(Math.abs( new Random().nextInt() % ((1000 * retrasoRandom) - 1000) ) + 1000)
		if (javascript)
			moverCamara = false
		def elemento = buscar(padre, xpathOrId, timeout, moverCamara, javascript)
		if (javascript)
			padre.javascriptExecutor.executeScript("arguments[0].click();", elemento);
		else{
			try {
				elemento.click()
			}
			catch(Exception e) {
				if (e.message.contains("element click intercepted")){
					if (printearMensajes)
						println "DEBUG: Click interceptado, intentando por JS"
					padre.javascriptExecutor.executeScript("arguments[0].click();", elemento);
				}
				else
					throw e
			}

		}
		return elemento
	}

	public WebElement clickearConTry(padre, xpathOrId, reintentos = 12, timeout = 1, javascript = false){
		while(reintentos)
			try {
				return clickear(padre, xpathOrId, timeout:timeout, javascript:javascript)
			}
			catch(Exception e) {
				reintentos--
				if (!reintentos)
					throw e
				if (javascript)
					Thread.sleep(timeout * 1000) // Lo hago acá porque adentro de buscar, si es JS se ignora el timeout
			}
	}

	public WebElement escribir(Map params, padre, String xpathOrId, String texto, Boolean vaciarAntes = null){
		Long timeout = params.get('timeout', null)
		Long reintentos = params.get('reintentos', 1)
		String remplazarSimbolo = params.get('remplazarSimbolo', null)
		Boolean vaciarAntesParam = params.get('vaciarAntes', false) || vaciarAntes
		Boolean javascript = params.get('javascript', false)
		Boolean modoHumano = params.get('modoHumano', false)
		return escribir(padre, xpathOrId, texto, vaciarAntesParam, timeout, reintentos, remplazarSimbolo, javascript, modoHumano)
	}
	public WebElement escribir(padre, String xpathOrId, String texto, Boolean vaciarAntes = null, Long timeout = null, Long reintentos = 1, String remplazarSimbolo = null, Boolean javascript = false, Boolean modoHumano = false) {
		if (timeout == null) timeout = 5

		WebElement elemento = buscar(padre, xpathOrId, timeout)
		while(reintentos > 0) {
			if(javascript) {
				if (vaciarAntes == true) {
					padre.javascriptExecutor.executeScript("arguments[0].value = '';", elemento)
				}
				if(modoHumano){
					texto.each {
						padre.javascriptExecutor.executeScript("arguments[0].value += arguments[1];", elemento, it.toString())
						// Disparar eventos para simular la entrada del usuario
						padre.javascriptExecutor.executeScript("arguments[0].dispatchEvent(new Event('input', {bubbles: true}));", elemento)
						padre.javascriptExecutor.executeScript("arguments[0].dispatchEvent(new Event('change', {bubbles: true}));", elemento)
						padre.javascriptExecutor.executeScript("arguments[0].dispatchEvent(new Event('keydown', {bubbles: true}));", elemento)
						padre.javascriptExecutor.executeScript("arguments[0].dispatchEvent(new Event('keyup', {bubbles: true}));", elemento)
						Thread.sleep(100) // Pausa breve para simular el tiempo entre pulsaciones de teclas
					}
				}
				else {
					padre.javascriptExecutor.executeScript("arguments[0].value = $texto;", elemento);
				}
			} else {
				escribir(elemento, texto, vaciarAntes)
			}

			if(reintentos > 1) {
				String valorElemento = remplazarSimbolo ? elemento.getAttribute('value').replace(remplazarSimbolo, '') : elemento.getAttribute('value')
				if(valorElemento == texto)
					break
				elemento = buscar(padre, xpathOrId, timeout)
			}
			Thread.sleep(500)
			reintentos--
		}
		return elemento
	}
	public WebElement escribir(WebElement elemento, String texto, Boolean vaciarAntes = false){
		elemento.with{
			if (vaciarAntes)
				clear()
			sendKeys(texto)
		}
		return elemento
	}

	def cambiarFrame(WebDriver driver, List<Integer> ruta, Integer timeout = 500){
		if (printearMensajes)
			println "Volviendo a Frame Raiz"
		driver.switchTo().defaultContent()
		ruta.each{
			cambiarFrame(driver, it, timeout)
		}
	}
	def cambiarFrame(WebDriver driver, Integer frame, Integer timeout = 500){
		if (frame == -1){
			if (printearMensajes)
				println "Intentando cambiar a Frame padre"
			driver.switchTo().parentFrame()
		}else{
			if (printearMensajes)
				println "Intentando cambiar a Frame $frame"
			int reintentos = 5
			while(reintentos){
				try{
					driver.switchTo().frame(frame)
					break
				}catch(e){
					reintentos--
					if (!reintentos) throw e
					Thread.sleep(timeout)
				}
			}
		}
		Thread.sleep(timeout)
	}

	def esperarCargaPagina(WebDriver driver, int timeout = 20){
		Thread.sleep(800) // Al estar arriba del timeout dinámico, esto es una espera mínima
		driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS)
		new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
	}

	public void esperarElemento(WebDriver driver, String xpath, String valorEsperado = null, int timeoutSecs = 30) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutSecs)
		wait.until { WebDriver drv ->
			try{
				WebElement elemento = buscar(drv, xpath, 1)
				if (valorEsperado)
					return elemento.text == valorEsperado
				else
					return !! elemento
			}
			catch(e) {
				Thread.sleep(1000)
				return false
			}
		}
	}

	def printearError(e, int numeroThread = 0, String mensaje2 = ''){
		String mensaje = "{" + new LocalDateTime().toString("dd/MM/yyyy HH:mm:ss") + "} Error Selenium" + (numeroThread ? " en Thread $numeroThread" : '') + ": $mensaje2"
		Auxiliar.printearError(e, mensaje)
	}

	def cerrar(WebDriver driver){
		try {
			println "\nCerrando Driver..."
			if (!Auxiliar.testingEnviroment && !driver.remoto) {
				driver.quit()
				println "	Driver Cerrado"
			} else if (Auxiliar.testingEnviroment) {
				println "	DEBUG: No cerramos el Driver porque estamos en ambiente de prueba"
			} else if (driver.remoto) {
				println "	DEBUG: Estamos en ambiente Remoto, cerrando driver en ${driver.timeoutRemoto} segundos."
				Thread.sleep(driver.timeoutRemoto * 1000)
				driver.quit()
				println "	Driver Cerrado tras esperar ${driver.timeoutRemoto} segundos."
			}

		}
		catch(Exception e) {
		}
	}

	public void sacarScreen(WebDriver driver, String nombre) {
		if (!driver){
			println "No sacamos screen porque el driver está cerrado. (!driver)"
			return
		}
		try {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)
			if (driver.remoto) {
				// Convierte la captura de pantalla a una cadena Base64
				byte[] fileContent = Files.readAllBytes(screenshot.toPath())
				String encodedString = java.util.Base64.getEncoder().encodeToString(fileContent)

				// Crea un script JavaScript para descargar la imagen en la máquina cliente
				String script = "var byteCharacters = atob('" + encodedString + "');\n" +
					"\tvar byteNumbers = new Array(byteCharacters.length);\n" +
					"\tfor (var i = 0; i < byteCharacters.length; i++) {\n" +
					"\t\tbyteNumbers[i] = byteCharacters.charCodeAt(i);\n" +
					"\t}\n" +
					"\tvar byteArray = new Uint8Array(byteNumbers);\n" +
					"\tvar blob = new Blob([byteArray], {type: 'image/png'});\n" +
					"\tvar link = document.createElement('a');\n" +
					"\tlink.href = URL.createObjectURL(blob);\n" +
					"\tlink.download = '" + nombre + ".png';\n" +
					"\tlink.click();"

				// Ejecuta el script en la máquina cliente a través del WebDriver
				driver.javascriptExecutor.executeScript(script);
			}
			String rutaScreen = "/tmp/tecnofind/screensSelenium/" + nombre + ".png"
			FileUtils.copyFile(screenshot, new File(rutaScreen))
			if (! Auxiliar.testingEnviroment) {
				rutaScreen = "'. fotoError $nombre tecnoFind'"
			}
			println "\nScreen tomada con éxito: $rutaScreen"
		} catch (Exception e) {
			String mensaje = e.message
			if (mensaje.contains("Session ID is null."))
				println "No sacamos screen porque el driver está cerrado. (Session ID is null.)"
			else if (mensaje.contains("target window already closed"))
				println "No sacamos screen porque el driver está cerrado. (target window already closed)"
			else if (mensaje.contains("chrome not reachable"))
				println "No sacamos screen porque el driver está cerrado. (chrome not reachable)"
			else
				Auxiliar.printearError(e, "Error sacando screen")
		}
	}
}