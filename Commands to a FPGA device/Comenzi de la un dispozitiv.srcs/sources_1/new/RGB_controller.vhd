LIBRARY IEEE;
USE IEEE.STD_LOGIC_1164.ALL;
USE IEEE.STD_LOGIC_UNSIGNED.ALL;
USE IEEE.NUMERIC_STD.ALL;

entity RGB_controller is
    port(clk, enable: in std_logic;
         rgb_led: out std_logic_vector(2 downto 0));
end RGB_controller;

architecture Behavioral of RGB_controller is
    --counter signals
    constant window: std_logic_vector(7 downto 0) := "11111111";
    signal windowcount: std_logic_vector(7 downto 0) := (others => '0');

    constant deltacountMax: std_logic_vector(19 downto 0) := std_logic_vector(to_unsigned(1000000, 20));
    signal deltacount: std_logic_vector(19 downto 0) := (others => '0');

    constant valcountMax: std_logic_vector(8 downto 0) := "101111111";
    signal valcount: std_logic_vector(8 downto 0) := (others => '0');

    --color intensity signals
    signal incVal: std_logic_vector(7 downto 0);
    signal decVal: std_logic_vector(7 downto 0);

    signal redVal: std_logic_vector(7 downto 0);
    signal greenVal: std_logic_vector(7 downto 0);
    signal blueVal: std_logic_vector(7 downto 0);
    
    --PWM registers
    signal rgbLedReg: std_logic_vector(2 downto 0);
begin

    window_counter:process(clk)
    begin
        if(rising_edge(clk)) then
            if windowcount < (window) then
                windowcount <= windowcount + 1;
            else
                windowcount <= (others => '0');
            end if;
        end if;
    end process;

    color_change_counter:process(clk)
    begin
        if(rising_edge(clk)) then
            if(deltacount < deltacountMax) then
                deltacount <= deltacount + 1;
            else
                deltacount <= (others => '0');
            end if;
        end if;
    end process;

    color_intensity_counter:process(clk)
    begin
        if(rising_edge(clk)) then
            if(deltacount = 0) then
                if(valcount < valcountMax) then
                    valcount <= valcount + 1;
                else
                    valcount <= (others => '0');
                end if;
            end if;
        end if;
    end process;

    incVal <= "0" & valcount(6 downto 0);

    --The folowing code sets decVal to (128 - incVal)
    decVal(7) <= '0';
    decVal(6) <= not(valcount(6));
    decVal(5) <= not(valcount(5));
    decVal(4) <= not(valcount(4));
    decVal(3) <= not(valcount(3));
    decVal(2) <= not(valcount(2));
    decVal(1) <= not(valcount(1));
    decVal(0) <= not(valcount(0));

    redVal <= incVal when (valcount(8 downto 7) = "00") else
 decVal when (valcount(8 downto 7) = "01") else
 (others => '0');
    greenVal <= decVal when (valcount(8 downto 7) = "00") else
 (others => '0') when (valcount(8 downto 7) = "01") else
 incVal;
    blueVal <= (others => '0') when (valcount(8 downto 7) = "00") else
 incVal when (valcount(8 downto 7) = "01") else
 decVal;
 
    --red processes
    red_comp:process(clk)
    begin
        if(rising_edge(clk)) then
            if((redVal) > windowcount) then
                rgbLedReg(2) <= '1';
            else
                rgbLedReg(2) <= '0';
            end if;
        end if;
    end process;

    --green processes
    green_comp:process(clk)
    begin
        if(rising_edge(clk)) then
            if((greenVal) > windowcount) then
                rgbLedReg(1) <= '1';
            else
                rgbLedReg(1) <= '0';
            end if;
        end if;
    end process;

    --blue processes
    blue_comp:process(clk)
    begin
        if(rising_edge(clk)) then
            if((blueVal) > windowcount) then
                rgbLedReg(0) <= '1';
            else
                rgbLedReg(0) <= '0';
            end if;
        end if;
    end process;

    rgb_led <= rgbLedReg when enable = '1' else "000";
end Behavioral;