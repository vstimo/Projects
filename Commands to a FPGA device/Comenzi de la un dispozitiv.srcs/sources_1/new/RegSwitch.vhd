library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity RegSwitch is
    port(clk, rd, rst:in std_logic;
         data:in std_logic_vector(7 downto 0);
         R_D:out std_logic_vector(7 downto 0));
end RegSwitch;

architecture Behavioral of RegSwitch is
begin
    process(clk,rd,data)
    begin
        if rising_edge(clk) then
            if rst='1' then
                R_D<= (others => '0');
            else
                if rd='1' then
                    R_d <= data;
                else
                    R_D<= (others => '0');
                end if;
            end if;
        end if;
    end process;

end Behavioral;
