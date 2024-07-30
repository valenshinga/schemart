package com.schemart.empleado

import grails.transaction.Transactional

@Transactional
class EmpleadoService {

    def listEmpleados(){
        return Empleado.list()
    }

}