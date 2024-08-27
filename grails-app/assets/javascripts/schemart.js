const formateador = new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS' });
function formatear(importe, pesos = false){
	const formateado = formateador.format(importe)
	return pesos ? formateado : formateado.substring(1)
}

if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}

function notify(mensaje, from, align, icon, type, animIn, animOut){
	$.growl({
		icon: icon,
		title: ' Mensaje: ',
		message: mensaje,
		url: ''
	},{
		element: 'body',
		type: type,
		allow_dismiss: true,
		placement: {
			from: from,
			align: align
		},
		offset: {
			x: 30,
			y: 30
		},
		spacing: 10,
		z_index: 999999,
		delay: 2500,
		timer: 12000,
		url_target: '_blank',
		mouse_over: false,
		animate: {
			enter: animIn,
			exit: animOut
		},
		icon_type: 'class',
		template: '<div data-growl="container" class="alert" role="alert">' +
		'<button type="button" class="close" data-growl="dismiss">' +
		'<span aria-hidden="true">&times;</span>' +
		'<span class="sr-only">Close</span>' +
		'</button>' +
		'<span data-growl="icon"></span>' +
		'<span data-growl="title"></span>' +
		'<span data-growl="message"></span>' +
		'<a href="#" data-growl="url"></a>' +
		'</div>'
	});
};

$(document).ready(function() {
	if($('#flashMessage').val()!='')
		notify($('#flashMessage').val(), 'top', 'center', 'fa fa-comments', 'success', 'animated fadeIn', 'animated fadeOut');
	
	if($('#flashError').val()!='')
		notify($('#flashError').val(), 'top', 'center', 'fa fa-comments', 'danger', 'animated fadeIn', 'animated fadeOut');
});

function llenarCombo(params) {
    var comboId = params.comboId;
    var ajaxUrlDiv = params.hasOwnProperty("ajaxUrlDiv") ? params.ajaxUrlDiv : null;
    var ajaxLink = params.hasOwnProperty("ajaxLink") ? params.ajaxLink : null;
    var idDefault = params.hasOwnProperty("idDefault") ? params.idDefault : null;
    var datosPasar = params.hasOwnProperty("parametros") ? params.parametros : null;
    var atributo = params.hasOwnProperty("atributo") ? params.atributo : "nombre";
    var identificador = params.hasOwnProperty("identificador") ? params.identificador : "id";
    var readOnly = params.hasOwnProperty("readOnly") ? params.readOnly : false;
    var data = params.hasOwnProperty("data") ? params.data : null;
    var onDataLoaded = params.hasOwnProperty("onDataLoaded") ? params.onDataLoaded : null;

    var combo = $('#' + comboId);
    combo.children('option').remove();

    // Check if idDefault is a string with a comma, split into an array, otherwise treat as array of one
    if (idDefault != null) {
        if (typeof idDefault === 'string' && idDefault.includes(',')) {
            idDefault = idDefault.split(',');
        } else {
            idDefault = [String(idDefault)];
        }
    } else {
        idDefault = [];
    }

    function populateCombo(data) {
        $.map(data, function(item) {
            var seleccionado = idDefault.includes(String(item[identificador])); // Check if the ID is in the idDefault array
            combo.append(new Option(item[atributo], item[identificador], seleccionado, seleccionado));
        });

        if (idDefault.length > 0)
            combo.val(idDefault).trigger("change"); // Set multiple values in select2

        if (readOnly)
            toggleReadOnlyCombo(comboId);

        if (onDataLoaded)
            onDataLoaded();
    }

    if (data != null) {
        populateCombo(data);
    } else {
        var urlDestino = ajaxLink != null ? ajaxLink : $('#' + ajaxUrlDiv).text();
        $.ajax(urlDestino, {
            dataType: "json",
            data: datosPasar
        }).done(function(data) {
            populateCombo(data);
        });
    }
}


function toggleReadOnlyCombo(comboId, valorAbsoluto = null){
	var combo = $('#' + comboId);
	var divId = "div" + comboId + "Text";
	var divConLabel = $("#" + divId);
	if(divConLabel.length){ // El div con texto ya existía, así que en lugar de volverlo a crear muestro y oculto respectivamente el combo y el div.
		var texto = (combo.select2('data')[0]!=null) ? combo.select2('data')[0].text : ''
		divConLabel.text(texto);
		if (valorAbsoluto == null){
			combo.next(".select2-container").toggle();
			divConLabel.toggle();
		}else if (valorAbsoluto == true){
			combo.next(".select2-container").hide();
			divConLabel.show();
		}else{
			combo.next(".select2-container").show();
			divConLabel.hide();
		}
	}else if (valorAbsoluto == null || valorAbsoluto == true) // Creo el div con el texto
		setTimeout(function() {
			var texto = (combo.select2('data')[0]!=null) ? combo.select2('data')[0].text : ''
			$("<div/>", {
			  text: texto,
			  id: divId,
			  "class": "texto-select2-readonly",
			  appendTo: combo.parent()
			});
			combo.next(".select2-container").hide(); // Oculto el combo
		}, 200);
}

function isNumberKey(evt){
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	if (charCode > 31 && (charCode < 48 || charCode > 57))
		return false;
	return true;
}

function leerFloat(nombreElemento, defecto = 0) {
	const elemento = $('#' + nombreElemento + '')
	if (! elemento.val())
		return defecto
	else
		return parsearFloat(elemento.val())
}

function parsearFloat(num){
	return parseFloat(num.replace(/\./g, "").replace(",", "."))
}

function leerInt(nombreElemento, defecto = 0) {
	const elemento = $('#' + nombreElemento)
	if (! elemento.val())
		return defecto
	else
		return parseInt($('#' + nombreElemento).val())
}

jQuery.fn.dataTableExt.oSort['numeric-comma-asc'] = function(a, b) {
	const x = (!a || a == "-") ? 0 : parseFloat(a.replace(/\./g, '').replace(',', '.'))
	const y = (!b || b == "-") ? 0 : parseFloat(b.replace(/\./g, '').replace(',', '.'))
	return ((x < y) ? -1 : ((x > y) ? 1 : 0));
};

jQuery.fn.dataTableExt.oSort['numeric-comma-desc'] = function(a, b) {
	const x = (!a || a == "-") ? 0 : parseFloat(a.replace(/\./g, '').replace(',', '.'))
	const y = (!b || b == "-") ? 0 : parseFloat(b.replace(/\./g, '').replace(',', '.'))
	return ((x < y) ? 1 : ((x > y) ? -1 : 0));
};

function generarFooterDT(datatable_id) {
	/**
	 * Genera un pie de página (footer) en un DataTable y devuelve un callback para ser utilizado en la opción "footerCallback" de DataTables.
	 *
	 * El callback calcula y muestra las sumatorias para cada columna que tiene el atributo 'sumatoria' en su elemento `th` correspondiente en `thead`. Además, permite mostrar un texto personalizado mediante el atributo 'footer-text'. No debe estar definido el tag 'tfoot' ya que este se genera automáticamente.
	 *
	 * @param {string} datatable_id - El ID del DataTable al cual agregarle la sumatoria por columnas.
	 * @returns {function} - Un callback que se puede usar como "footerCallback" en las opciones de un objeto DataTable.
	 *
	 * @example
	 * 
	 * const footerCallback = generarFooterDT('miDataTable');
	 * $('#miDataTable').DataTable({
	 *     "footerCallback": footerCallback
	 * });
	 *
	 * // Ejemplo de un 'th' con todos los parámetros opcionales:
	 * // <th footer-text="Texto personalizado">Anterior</th>
	 * // <th sumatoria symbol="%" symbol-pos="right" footer-id="sumatoriaMaximo">Máximo mensual</th>
	 *
	 * ### Parámetros opcionales de los `th`:
	 * - sumatoria: Define que la columna será sumada. No necesita valor.
	 * - symbol (opcional, default = '$'): El símbolo que se mostrará junto a la sumatoria en el pie de página.
	 * - symbol-pos (opcional, default = 'left'): Define la posición del símbolo. Los valores pueden ser 'left' o 'right'.
	 * - footer-id (opcional): Define un ID para el `th` del footer, facilitando su selección para operaciones de JS posteriores.
	 * - footer-text (opcional): Define un texto personalizado para mostrar en el footer de la columna, este parámetro anula el efecto de la sumatoria para esa columna.
	 * - intVal (opcional): No muestra comas en la sumatoria.
	 *
	 */
	let sumatoriaIndices = [];
	let colSpanCounter = 0;
	let tfootHTML = '<tr>';
	let colDetails = [];

	$('#' + datatable_id).append('<tfoot></tfoot>');

	$('#' + datatable_id + ' thead tr:not(.filters) th').each(function (index) {
		let colId = $(this).attr('footer-id') ? ' id="' + $(this).attr('footer-id') + '"' : '';
		let footerText = $(this).attr('footer-text');
		let symbol = $(this).attr('symbol') ? $(this).attr('symbol') : '$';
		let symbolPosition = $(this).attr('symbol-pos') ? $(this).attr('symbol-pos') : 'left';
		let intVal = $(this).is('[intVal]');
		if (footerText !== undefined) {
			if (colSpanCounter > 0) {
				tfootHTML += '<th colspan="' + colSpanCounter + '"></th>';
				colSpanCounter = 0;
			}
			tfootHTML += '<th' + colId + '>' + footerText + '</th>';
		} else if ($(this).is('[sumatoria]')) {
			if (colSpanCounter > 0) {
				tfootHTML += '<th colspan="' + colSpanCounter + '"></th>';
				colSpanCounter = 0;
			}
			tfootHTML += '<th' + colId + '></th>';
			sumatoriaIndices.push(index);
			colDetails.push({ symbol, symbolPosition, intVal });
		} else {
			colSpanCounter++;
		}
	});

	if (colSpanCounter > 0) {
		tfootHTML += '<th colspan="' + colSpanCounter + '"></th>';
	}

	tfootHTML += '</tr>';
	$('#' + datatable_id + ' tfoot').html(tfootHTML);

	return function (row, data, start, end, display) {
		var api = this.api();
		var columna;

		for (var coln = sumatoriaIndices.length - 1; coln >= 0; coln--) {
			columna = sumatoriaIndices[coln];
			const { symbol, symbolPosition, intVal } = colDetails[coln];
			let sum = api
				.column(columna, { page: 'all', search: 'applied' })
				.data()
				.reduce(function (a, b) {
					const numA = typeof a === 'number' ? a : a == undefined || a == "" ? 0 : parsearFloat(a);
					const numB = typeof b === 'number' ? b : b == undefined || b == "" ? 0 : parsearFloat(b);
					return numA + numB;
				}, 0);
			sum = formatear(sum, false);
			if (intVal)
				sum = sum.split(',')[0]			
			if (symbolPosition === 'left') {
				sum = symbol + sum;
			} else {
				sum = sum + symbol;
			}
			$(api.column(columna).footer()).html(sum);
		}
	};
}

function puntoCadaMil(num, decimales = 2){
	return num.toFixed(decimales).replace('.',',').replace(/\B(?=(\d{3})+(?!\d))/g, ".")
}