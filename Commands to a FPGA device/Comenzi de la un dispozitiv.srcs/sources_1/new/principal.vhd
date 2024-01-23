library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity principal is
  Port (clk, rx, rst, start: in std_logic;
        sw: in std_logic_vector(7 downto 0);
        led: out std_logic_vector(15 downto 0);
        rgb: out std_logic_vector(2 downto 0);
        tx : out std_logic);
end principal;

architecture Behavioral of principal is
signal rstEnable, rgbEnable, rxRdy, txRdy: std_logic:='0';
signal rxToReg, outRegLeds, regToTx: std_logic_vector(7 downto 0) := (others => '0');
signal outDecoder, outAnimationBlock: std_logic_vector(15 downto 0);
signal startEnable: std_logic := '0';
begin

debounce: entity Work.debouncer port map(rstEnable,rst,clk); 

receive: entity WORK.receiver port map(clk, rx, rxRdy, rxToReg);  

registruLeduri: entity WORK.RegLeds port map(clk, rxRdy, rstEnable, rxToReg, outRegLeds);

decoder: entity WORK.DecodificatorAscii port map(clk, rstEnable, outRegLeds, rgbEnable, outDecoder);

animation: entity WORK.AnimationBlock port map(clk, rstEnable, outRegLeds, outDecoder, outAnimationBlock);

rgbPart: entity WORK.RGB_controller port map(clk, rgbEnable, rgb);

registrulSwitchuri: entity WORK.RegSwitch port map(clk, txRdy, rstEnable, sw, regToTx);
             
debouncerStart: entity WORK.debouncer port map(startEnable, start, clk);

trans: entity WORK.transmitter port map(clk, startEnable, regToTx, tx, txRdy);
               
led <= outAnimationBlock;

end Behavioral;
