package model;

/**
 * Clasa(record) pentru inregistrarea facturii fiecarei comenzi inserate in baza de date
 * @param idOrder
 * @param totalPrice
 */
public record Bill(int idOrder, int totalPrice) {
}
