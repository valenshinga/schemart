package com.tecnofind.selenium

import com.tecnofind.persona.PersonaCommand
import com.tecnofind.busqueda.BuscarCommand
import com.tecnofind.persona.Persona
import com.tecnofind.persona.NombreService
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.*


class SeleniumService implements SeleniumTrait {
    
    def accessRulesService
    def nombreService

    def eliminarAcentos(String str) {
        if (str) {
            def mapaAcentos = ['á':'a', 'é':'e', 'í':'i', 'ó':'o', 'ú':'u', 'Á':'A', 'É':'E', 'Í':'I', 'Ó':'O', 'Ú':'U']
            mapaAcentos.each { acento, sinAcento ->
                str = str.replace(acento, sinAcento)
            }
        }
        return str
    }

    def separarStrings(String string, String separador){
        List<String> partesString = string.split(separador)
        if (partesString.size() == 2) {
            return [partesString[0], partesString[1]]
        }
        else if (partesString.size() == 3) {
            return [partesString[0], partesString[1], partesString[2]]
        }
        else if (partesString.size() == 4) {
            return [partesString[0], partesString[1], partesString[2], partesString[3]]
        }
        else if (partesString.size() == 5) {
            return [partesString[0], partesString[1], partesString[2], [partesString[3] + " " + partesString[5]]]
        }
        else{
            return [partesString[0], null]
        }
    }

    String normalizeName(String name) {
        return name.trim().toUpperCase().replaceAll(/\s+/, " ").replaceAll(/ ,/, ",")
    }

    Boolean revisaResultadoCorrecto(listNombre, BuscarCommand buscarCommand = null, Persona persona = null) {
        def objetoAComparar = buscarCommand ?: persona
        if (objetoAComparar) {
            listNombre = listNombre.collect { normalizeName(it) }
            def propiedades = [
                objetoAComparar.primerNombre,
                objetoAComparar.segundoNombre,
                objetoAComparar.apellidoPadre,
                objetoAComparar.apellidoMadre
            ].findAll { it != null }

            return propiedades.every { property ->
                normalizeName(property) in listNombre
            }
        }
        return false
    }

    String quitarArticulos(String name, Boolean hayDe = false) {
        if(hayDe) {
            return name.toUpperCase().replaceAll("DE ", "").trim();
        } else {
            return name
        }
    }

    public boolean contieneDE(String texto) {
        if (texto.contains("DE ")) {
            return true;
        } else {
            return false;
        }
    }

    def eliminarCodigoArea(String telefonoRecibido) {
        if (!telefonoRecibido) return
        return telefonoRecibido.replaceAll("\\+34","").replaceAll("\\s","")
    }

    def obtenerNombreyApellidos(PersonaCommand personaBuscada, String nombreCompleto){
        if (nombreCompleto){
            def nombreSeparado = nombreCompleto.split(" ")
            def apellidoPadre
            def apellidoMadre
            def primerNombre
            def segundoNombre

            if (nombreSeparado.size() >= 4){
                apellidoPadre = nombreSeparado[0].replace(".", "").replace(",", "")
                apellidoMadre = nombreSeparado.size() > 1 ? nombreSeparado[1].replace(".", "").replace(",", "") : ""
                primerNombre = nombreSeparado[2].replace(".", "").replace(",", "")
                segundoNombre = nombreSeparado.size() > 2 ? nombreSeparado[3..-1].join(" ").replace(".", "").replace(",", "") : ""
            }
            else if(nombreSeparado.size() == 3) {
                apellidoPadre = nombreSeparado[0].replace(".", "").replace(",", "")
                apellidoMadre = nombreSeparado.size() > 1 ? nombreSeparado[1].replace(".", "").replace(",", "") : ""
                primerNombre = nombreSeparado[-1].replace(".", "").replace(",", "")
                segundoNombre = null
            }
            else {
                apellidoPadre = nombreSeparado[0].replace(".", "").replace(",", "")
                apellidoMadre = null
                primerNombre = nombreSeparado[-1].replace(".", "").replace(",", "")
                segundoNombre = null
            }
            personaBuscada.apellidoPadre = apellidoPadre
            personaBuscada.apellidoMadre = apellidoMadre
            personaBuscada.primerNombre = primerNombre
            personaBuscada.segundoNombre = segundoNombre
        }
        return personaBuscada
    }

    def traducirNombres(BuscarCommand buscarCommand) {
        BuscarCommand buscarCommandTraducido = buscarCommand.clone()
        if(buscarCommand.primerNombre){
            String primerNombreTraducido
            primerNombreTraducido = nombreService.getTraduccion(buscarCommand.primerNombre)
            if(primerNombreTraducido) buscarCommandTraducido.primerNombre = primerNombreTraducido
        }
        if(buscarCommand.segundoNombre){
            String segundoNombreTraducido
            segundoNombreTraducido = nombreService.getTraduccion(buscarCommand.segundoNombre)
            if(segundoNombreTraducido) buscarCommandTraducido.segundoNombre = segundoNombreTraducido
        }
        return buscarCommandTraducido
    }

    def tieneSinonimos(BuscarCommand buscarCommand) {
        if(buscarCommand.primerNombre){
            String primerNombreTraducido
            primerNombreTraducido = nombreService.getTraduccion(buscarCommand.primerNombre)
            if(primerNombreTraducido) return true
        }
        if(buscarCommand.segundoNombre){
            String segundoNombreTraducido
            segundoNombreTraducido = nombreService.getTraduccion(buscarCommand.segundoNombre)
            if(segundoNombreTraducido) return true
        }
        
        return false
    }

    def limpiarInput(WebDriver driver, String path) {
        WebElement input = driver.findElement(By.xpath(path)).clear()
        return
    }
}
