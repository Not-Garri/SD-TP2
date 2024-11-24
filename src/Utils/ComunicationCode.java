/* ------------------------------------------------ *
 *  Trabalho realizado por Rodrigo Garraio, n.23599 *
 *    3. Ano, 1. Semestre, Sistemas Distribuidos    *
 * ------------------------------------------------ */

package Utils;

/**
 * <h2>ComunicationCode</h2></p>
 * Codigos que vao ser passados entre o cliente e o servidor
 */
public enum ComunicationCode {
    LOGIN_SUCCESS,                          //Indica que o login foi feito com sucesso
    LOGIN_FAILURE,                          //Indica que o login falhou
    OPERATION_SUCCESS,                      //Indica que a operacao em questao foi feita com sucesso
    OPERATION_FAILURE,                      //Indica que a operacao em questao falhou
    PRODUCT_FOUND,                          //Indica que o produto em questao foi encontrado
    PRODUCT_NOT_FOUND;                      //Indica que o produto em questao nao foi encontrado

    //Permite instanciar variaveis do tipo ComunicationCode
    ComunicationCode() {
    }

}
