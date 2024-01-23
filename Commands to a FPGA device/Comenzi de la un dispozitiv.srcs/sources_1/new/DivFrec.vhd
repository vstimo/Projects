library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_UNSIGNED.all;
use IEEE.std_logic_arith.all;

entity DivFrec is
    port(clk, rst: in std_logic;
         clk_nou: out std_logic);
end DivFrec;

architecture Behavioral of DivFrec is
    signal num: std_logic_vector(21 downto 0):="0000000000000000000000";
begin
    process(clk)
    begin
        if rst='1' then
            num<="0000000000000000000000";
        elsif rising_edge(clk) then
            num<=num+1;
        end if;
        if num = "1111111111111111111111" then
            clk_nou<='1';
        else clk_nou<='0';
        end if;
    end process;

end Behavioral;
